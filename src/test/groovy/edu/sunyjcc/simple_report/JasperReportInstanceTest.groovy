package edu.sunyjcc.simple_report

/** Test the JasperReportInstance class. */
public class JasperReportInstanceTest extends GroovyTestCase {

  /** Get the source directory for the parameter forms, reports, etc. */
  File getSourceDir(String root = 'dir1') {
    File f = new File("src/samples/$root");
  }

  /** Get a subdirectory of the source directory */
  File getSourceSubDir(String subDirName) {
    new File(getSourceDir(), subDirName);
  }

  File getJrxmlDir() {
    getSourceSubDir('jrxml');
  }

  JasperReportInstance getAppsRpt() {
    getReportObjectFactory().getJasperReport('apps');
  };

  /** Get a ClientEnv with a system parameter */
  ClientEnv getClientEnv() {
    def ce = new ClientEnv();
    ce.systemParams.a = "a"
    ce.systemParams.b = {-> 2 + 2}
    return ce;
  }

  /** Get a FileSourceFactory with the source directory as its root. */
  FileSourceFactory getFileSourceFactory(String root = 'dir1') {
    def fsf = new FileSourceFactory(getSourceDir(root))
    assert fsf
    return fsf
  }
  
  ReportObjectFactory getReportObjectFactory(String root = 'dir1') {
    def fsf = getFileSourceFactory(root);
    def rof = new ReportObjectFactory(fsf, getClientEnv());
    assert rof;
    assert rof.cache
    rof;
  }

  def banner(String text) {
    println "********** $text **********"
  }

  void testCreateJasperReport() {
    banner "testCreateJasperReport";
    JasperReportInstance r = getReportObjectFactory().getJasperReport('apps')
    assert r
    def sourceFile = new File(getJrxmlDir(), 'apps.jrxml');
    assert r.source == "${sourceFile.canonicalFile}"
    assert r.factory
    assert r.name == "apps"
    assert r.jrxmlFile == new File(r.source);
    def jasperDir = new File(sourceFile.canonicalFile.getParentFile(), 'jasper')
    String jasperFileName = sourceFile.name
        .replaceAll(/(?i)\.jrxml$/, '.jasper');
    File jasperFile = new File(jasperDir, jasperFileName)
    assert r.jasperFile == jasperFile
  }

  void testParameterForm() {
    banner "testParameterForm";
    JasperReportInstance r = getReportObjectFactory().getJasperReport('apps')
    ParamFormValue p = r.params
    def pe = p.export();
    assert p;
    assert pe.a == [name: 'a',
                    type: 'SYSTEM',
                    description: 'a',
                    label: 'a',
                    'default': 'a',
                    value: 'a'
    ];
    assert pe.b.name ==  'b';
    assert pe.b.type ==  'SYSTEM';
    assert pe.b.description ==  'b';
    assert pe.b.label ==  'b';
    assert pe.b.default == {-> 2 + 2}();
    assert pe.b.value == {-> 2 + 2}();
    assert pe.b == [name: 'b',
                    type: 'SYSTEM',
                    description: 'b',
                    label: 'b',
                    'default':{-> 2 + 2}(),
                    value:{-> 2 + 2}()];
  }

  void testParameterForm2() {
    banner "testParameterForm2"
    println "Using second source directory"
    def f = getReportObjectFactory('dir2');
    def jr = f.getJasperReport('immunization_rec')
    ParamFormValue p = jr.params;
    assert p 
    def pe = p.export()
    assert pe.pidm == [name: "pidm", 
                       type: "NUMBER", 
                       description: "pidm", 
                       label: "Pidm", 
                       "default": null, 
                       value: null];
    println pe.keySet().getClass()
    pe.keySet() == ['param_choice', 'pidm', 'popsel_app', 
                    'popsel_selection', 
                    'popsel_creator', 
                    'popsel_user'] as Set
  }

  void testRunJasperReport() {
    banner "testRunJasperReport";
    JasperReportInstance r = getAppsRpt();
    // The next statment will have to change
    def o = new StringWriter()
    //r.run(OutputFormat.pdf, r.getParamFormValue(), new new PrintWriter(o));
    //assert o.toString() == "";
  }

  void testOutputFormats() {
    banner "testOutputFormats";
    JasperReportInstance r = getAppsRpt();
    def expectedFormats = [OutputFormat.pdf];
    assert r.getOutputFormats() == expectedFormats;
  }

  void testSubreportList() {
    banner "testSubreportList"
    def f = getReportObjectFactory('dir2');
    def r = f.getJasperReport('housing_pdf');
    println "subreports array class: ${r.subreports.getClass()}"
    ArrayList expectedSubreports = ["parent_info.jasper", 
                                    "emergency_info.jasper", 
                                    "cellphone_info.jasper"]
    (0..(r.subreports.size() - 1)).each { i->
      assert r.subreports[i].getClass() == java.lang.String
      assert r.subreports[i] == expectedSubreports[i]
    }
    assert r.subreports == expectedSubreports
  }

  void testRun() {
    banner "testRun";
    JasperReportInstance r = getAppsRpt();
    def o = new FileOutputStream('build/apps.pdf')
    r.run(OutputFormat.pdf, r.paramFormValue, o);
    o.flush()
    o.close()
    // No database connection is here, so there's no output.
    assert new File('build/apps.pdf').text.size() == 0;
  }

  void testRun2() {
    banner "testRun";
    JasperReportInstance r = getReportObjectFactory().getJasperReport('crse_sect_attributes');;
    def o = new FileOutputStream('build/apps.pdf')
    r.run(OutputFormat.pdf, r.paramFormValue, o);
    o.flush()
    o.close()
    // No database connection is here, so there's no output.
    assert new File('build/apps.pdf').text.size() == 0;
  }
}
