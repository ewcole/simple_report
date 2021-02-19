package edu.sunyjcc.simple_report;

import groovy.xml.*;
import groovy.sql.*;
import groovy.util.slurpersupport.NodeChild;
import java.sql.Connection;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperCompileManager;

/** This class represents a Jasper Report, compiling and 
 *  running it as needed.
 */
public class JasperReportInstance implements Runnable, Exportable {

  Sql sql;

  Connection getConnection() {
    sql.getConnection();
  }

  /** A map of exporters to produce output of all desired output types */
  static def exporters = [
    new JasperReportPdfExporter(),
  ].inject([:]) {
    exporterMap, exporter ->
      exporterMap[exporter.outputFormat.code] = exporter;
      return exporterMap;
  }

  /** The factory that birthed this object.  It will be used for creating
   *  parameter forms. 
   */
  ReportObjectFactory factory;

  /** The name of the report */
  String name;

  /** The file containing the report definition */
  File jrxmlFile;

  /** The directory containing the report definitions */
  File jrxmlDir;

  NodeChild parsedSource;

  ArrayList subreports = [];

  /** The compiled report file */
  File jasperFile;

  ParamFormValue params;
  /** Get a param form value for the object.*/

  ParamFormValue getParamFormValue() {
    return params;
  }

  String source;

  File getJasperDir() {
    def jasperDir = new File(jrxmlFile.getParentFile(), 'jasper')
    if (!jasperDir.exists()) {
      jasperDir.mkdirs();
    }
    jasperDir;
  }

  def compileReport(String reportFile) {
    File jasperDir = getJasperDir();
    assert jasperDir
    assert jrxmlDir
    String reportFileName = new File(reportFile).name
        .replaceAll(/\.jasper$/, '')
        .replaceAll(/\.jrxml/, '');
    File sourceFile = new File(jrxmlDir,  "${reportFileName}.jrxml");
    File outputFile = new File(jasperDir, "${reportFileName}.jasper");
    // println("sourceFile = ${sourceFile.canonicalFile}");
    // Now, check to see if we need to compile this file.
    if (! outputFile.exists() 
        || outputFile.lastModified() < sourceFile.lastModified()) {
      println "Compiling $sourceFile"
      JasperCompileManager.compileReportToFile(sourceFile.canonicalPath, 
                                               outputFile.canonicalPath) 
    }
    // Copy any images in the file to the working directory
    def images = new XmlSlurper().parse(sourceFile)
                          .depthFirst()
                          .findAll {it.name() == 'imageExpression'}
                          .collect{it.text() as String}
                          .collect {it.replaceAll(/"(.*)"/, "\$1")};
    def ant = new AntBuilder();
    images.each {
      img ->
        def imgFile = new File(img);
        if (imgFile.parent) {
          println "Absolute reference in report ${sourceFile.name}: $img"
        } else {
          imgFile = new File(jrxmlDir, img);
        }
        if (imgFile.exists()) {
          ant.copy(file: imgFile.getCanonicalPath(),
                   todir: jasperDir.canonicalPath) 
        } else {
          println "File not found: $imgFile"
        }
    }
  // Return a reference to the compiled file.
    outputFile.canonicalFile;
  }

  def compileReport(File reportFile) {
    // Call the compile function with the canonical file name
    compileReport(reportFile.canonicalPath);
  }

  /** Compile all of the reports, if necessary */
  public def compile() {
    subreports.each {
      compileReport(it);
    }
    compileReport(jrxmlFile)
  }

  public def export() {
    [name: name,
     source: source,
     jrxmlFile: jrxmlFile,
     jasperFile: jasperFile];
  }
  //////////////////////////////////////////////////////////////////////////
  // These methods will set up parts of the object
  //////////////////////////////////////////////////////////////////////////

  /** Set up the variables pointing to the report files and make sure they
   *  exist.
   */
  private loadSourceFiles() {
    jrxmlFile = new File(source).canonicalFile;
    assert jrxmlFile.exists()
    def jrxmlName = jrxmlFile.name
    assert jrxmlName =~ /(?i)\.jrxml$/
    jrxmlDir = jrxmlFile.parentFile;
    assert jrxmlDir
    println "jrxmlDir = $jrxmlDir";
    name = (jrxmlFile.name).replaceAll(/(?i)\.jrxml$/, '');
    parsedSource = new XmlSlurper().parse(jrxmlFile);
    subreports = parsedSource.depthFirst()
                          .findAll {it.name() == 'subreportExpression'}
                          .collect{it.text() as String}
                          .collect {it.replaceAll(/"(.*)"/, "\$1")}
    jasperFile = compile();
  }

  /** Search for a parameter form for the report. */
  private loadParamForm() {
    def paramTypes = [
      /java.math.BigDecimal/: 'NUMBER',
      /.*/:     'STRING'
    ];
    ParamForm superParamForm;
    try {
      superParamForm = factory.getParamForm(this.name);
    } catch (BuildException e) {
      // Cannot create the parameter form
      superParamForm = new ParamForm();
      superParamForm.reportObjectFactory = factory;
    }
    println "superParamForm = ${superParamForm?.export()}"
    ParamForm paramForm = new ParamForm(superParamForm);
    paramForm.reportObjectFactory = factory;
    //def sysParams = factory.clientEnv.systemParams.list().collect {it.name};
    parsedSource.parameter.each {
      prm ->
      def paramClass = "${prm.@class}"
      try {
        paramForm.addParam(factory.build {
          def pType = paramTypes.inject(null) {
            currType, re ->
              currType?:(paramClass =~ re.key)?re.value:null;
          }
          param(name: "${prm.@name}",
                description: "${prm.parameterDescription}",
                type: pType)
          
        });
      } catch (BuildException e) {
      }
    }
    this.params = paramForm.getParamFormValue()
  }

  /** Change this from an empty shell to a fully functioning report object */
  JasperReportInstance setUp() {
    loadSourceFiles();
    loadParamForm();
    return this;
  }

  /** Get a list of the supported output types */
  ArrayList<OutputFormat> getOutputFormats() {
    exporters.keySet().collect {
      exporters[it].outputFormat
    }
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param outputFormat This tells us what kind of output you want to create,
   *                      For example, you might want HTML or CSV.
   *  @param paramFormValue An object that gives us the parameters to be used 
   *                        when creating the output for the report.
   *  @param out       An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, 
              OutputStream out) {
    /** First, validate the parameters */
    if (! params.validate()) {
      return false;
    }
    // Check the output format against the valid list.
    if (!exporters.containsKey(outputFormat.code)) {
      return false;
    }
    if (!sql) {
      return false
    }
    JasperReportExporter exporter = exporters[outputFormat.code]
    // Assume that all Jasper Report objects are present and already compiled.
    JasperPrint jp = JasperFillManager.fillReport("$jasperFile", 
                                                  params.getValueMap(), 
                                                  connection);
    exporter.exportReport(jp, out);
  }
  
  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param outputFormat This tells us what kind of output you want to create,
   *                      For example, you might want HTML or CSV.
   *  @param paramFormValue An object that gives us the parameters to be used 
   *                        when creating the output for the report.
   *  @param out       An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, 
              Writer out) {
    assert out instanceof OutputStream
  }
  JasperReportInstance init(def args) {
    if (args.sql && args.sql instanceof groovy.sql.Sql) {
      this.sql = args.sql;
    }
    return this;
  }

  /** Is this object valid and ready to run? */
  boolean validate() {}

  public JasperReportInstance(String source, ReportObjectFactory factory) {
    this.source = source;
    this.factory = factory;
    setUp()
  }
}
