package edu.sunyjcc.simple_report;

/** A report that can be defined in a small Groovy script, producing an 
 *  output
 */
public class SimpleReport implements Exportable, Buildable, Runnable {

  String getBuildDocHtml() {
    ("This creates a report that can produce a data file in several "
     + "formats, including CSV.")
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [name:    [desc: "The name of the report.  It defaults to the name of the script."],
     version: [desc: "The version number for this report."],
     title:   [desc: "The report's title."]]
  }

  String source;

  /* Report-level properties *************/
  /** The report name */
  String name    = "";

  /** The report's title */
  String title   = "";

  /** The report version */
  String version = "";

  /** A description of the report */
  String description;

  
  /** The active parameters for the report
   */
  private ParamForm params

  /** The columns that this report will produce */
  private ColumnList columns

  /** The engine that will power the report */
  private QueryEngine queryEngine

  /* End of report-level properties *************/


  public void setReportParams(ArrayList l) {
    // Don't let outsiders mess with params.
  }

  SimpleReport init(HashMap args) {
    assert queryEngine
    if (queryEngine) {
      queryEngine.init(args)
    }
    if (params) {
      params.init(args)
    }
    return this
  }

  /** Return a HashMap representing this report object. */
  def export() {
    [name: this.name,
     title: this.title,
     version: this.version,
     description: this.description,
     params: this.params?.export(),
     columns: this.columns?.export(),
     queryEngine: this.queryEngine?.export(),
    ]
  }

  /** Add a parameter to the report 
   *  @param p This parameter will be added to the end of the list.
   */
  public void addParam(Param p) {
    if (!params) {
      this.params = new ParamForm()
    }
    params.params[p.name] = p
  }

  /** Copies values from arguments into hashmap
   *  @param params A hash map that contains the name
   *                and version of the report.
   */
  private void newReport(HashMap params) {
    if (params.name) {
      this.name = params.name
    }
    if (params.version) {
      this.version = params.version
    }
    if (params.title) {
      this.title = params.title
    }
  }

  /** Create an unnamed report. Calls newReport() to do its stuff.
   */
  public SimpleReport() {
    newReport([version: '0.0.0'])
  }

  /** Create a report with values given in a hash map
   *  @param params A hash map that contains the name
   *                and version of the report.
   */
  public SimpleReport(HashMap params) {
    newReport(params);
  }
 
  /** Execute the report and return the result. */
  public ResultSet execute(ParamFormValue params) {
    def p = (this.params?.getParamFormValue())?:new ParamFormValue();
    p.setParamValues(params)
    queryEngine.execute(p)
  }
 
  /** Execute the report and return the result. */
  public ResultSet execute(HashMap params) {
    this.params.setParamValues(params)
    queryEngine.execute(this.params)
  }
 
  // Methods we need to implement for the Runnable interface

  /** Get a param form value for the object.*/
  @Override
  ParamFormValue getParamFormValue() {
    new ParamFormValue((this.params)?:(new ParamForm()))
  }

  /** Get a list of the supported output types */
  @Override
  ArrayList<OutputFormat> getOutputFormats() {
    new SimpleReportInstance(this).getOutputFormats()
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
    new SimpleReportInstance(this).run(outputFormat, paramFormValue, out)
  }

  /** Is this object valid and ready to run? */
  boolean validate() {
    return false
  }

}