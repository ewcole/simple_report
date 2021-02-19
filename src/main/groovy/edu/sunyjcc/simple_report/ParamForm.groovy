package edu.sunyjcc.simple_report;

import java.util.*

/** An ordered list of parameters that can be used with the SimpleReports 
 *  system.
 */
public class ParamForm implements Buildable, Exportable, Runnable {

  /** The factory that created this object */
  ReportObjectFactory reportObjectFactory;

  public void setReportObjectFactory(ReportObjectFactory reportObjectFactory) {
    this.reportObjectFactory = reportObjectFactory;
    // Copy system parameters into this form as standard parameters.
    refreshSystemParams();
  }

  /** Copy system parameters into this parameter form. */
  public ParamForm refreshSystemParams() {
    def sp = this.reportObjectFactory?.clientEnv?.systemParams;
    if (sp) {
      sp.keySet().each {
        sysParamName ->
        def p = this.getParam(sysParamName);
        if (!p) {
          this.addParam(sp.params[sysParamName].getParam());
        }
      }
    }
    return this;
  }
  
  String getBuildDocHtml() {
    ("This creates a parameter form.  Prototypical inheritance is available "
     + "by using the copyFrom option.") 
  }


  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [copyFrom: [
        desc: "The name of another parameter form that will serve as a prototype for this one.  The parameters and validation from the prototype will be copied into this form."
      ]]
  }

  String source;

  // The parameters
  private HashMap<String,Param> params = [:];
  private boolean isValid;

  public Param getParam(String paramName) {
    if (params.containsKey(paramName)) {
      params[paramName];
    }
  }
  
  public ParamForm addParam(Param param) {
    println ("in addParam(${param})");
    if (this.params.containsKey(param.name)) { 
      throw new BuildException("ParamForm already contains parameter ${param.name}");
    }
    this.params[param.name] = param;
    assert this.params.containsKey(param.name)
    return this;
  }

  public ParamForm copyFrom(ParamForm prototype) {
    prototype.params.each {
      paramName, param ->
        this.addParam(param);
        assert this.params.containsKey(paramName);
    }
    return this;
  }

  /** Return the parameter items as a list */
  def export() {
    params.keySet().inject([:]) {
      map, paramName ->
        map[paramName] = this.params[paramName].export()
        return map
    }
  }

  @Override
  public ParamFormValue getParamFormValue() {
    new ParamFormValue(this)
  }

  /** Access the isValid property */
  public boolean getIsValid() {
    return isValid;
  }

  /** Perform whatever initialization is needed for the parameters. */
  ParamForm init(HashMap args) {
    params.each {it.value.init(args)}
    return this
  }

  /** Test all parameters and see if they're OK. */
  boolean validate() {
    return true
  }
  
  /** Get a list of the supported output types */
  ArrayList<OutputFormat> getOutputFormats() {
    getParamFormValue().getOutputFormats()
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  @Override
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, Writer out) {
    getParamFormValue().setParamValues(paramFormValue).run(outputFormat, out)
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  @Override
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, OutputStream out) {
    getParamFormValue().setParamValues(paramFormValue).run(outputFormat, out)
  }

  //////////////////////////////////////////////////////////////////////
  // Constructors                                                     //
  //////////////////////////////////////////////////////////////////////
  public ParamForm() {
  }

  public ParamForm(ReportObjectFactory reportObjectFactory) {
    this.setReportObjectFactory(reportObjectFactory);
  }

  public ParamForm(SimpleReport simpleReport) {
    this.setReportObjectFactory(simpleReport.reportObjectFactory);
  }

  /** Build a new parameter form based on another parameter form 
   *  @param  prototype The other ParamForm that we will use as a model.
   */  
  public ParamForm(ParamForm prototype) {
    println "Prototype param form"
    this.copyFrom(prototype)
  }
}
