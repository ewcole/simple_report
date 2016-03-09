package edu.sunyjcc.simple_report;

/** A job that can be defined in a small Groovy script, producing an 
 *  output
 */
public class SimpleJob implements Exportable, Buildable, Runnable {

  String getBuildDocHtml() {
    ("This creates a job that can produce a data file in several "
     + "formats, including CSV.")
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [name:    [desc: "The name of the job.  It defaults to the name of the script."],
     version: [desc: "The version number for this job."],
     title:   [desc: "The job's title."]]
  }

  String source;

  /* Job-level properties *************/
  /** The job name */
  String name    = "";

  /** The job's title */
  String title   = "";

  /** The job version */
  String version = "";

  /** A description of the job */
  String description;

  
  /** The active parameters for the job
   */
  private ParamForm params


  public setParams(ParamForm params) {
    this.params = params;
  }

  /** The columns that this job will produce */
  private ColumnList columns

  /** The engine that will power the job */
  private QueryEngine queryEngine

  /* End of job-level properties *************/


  public void setJobParams(ArrayList l) {
    // Don't let outsiders mess with params.
  }

  SimpleJob init(HashMap args) {
    assert queryEngine
    if (queryEngine) {
      queryEngine.init(args)
    }
    if (params) {
      params.init(args)
    }
    return this
  }

  /** Return a HashMap representing this job object. */
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

  /** Add a parameter to the job 
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
   *                and version of the job.
   */
  private void newJob(HashMap params) {
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

  /** Create an unnamed job. Calls newJob() to do its stuff.
   */
  public SimpleJob() {
    newJob([version: '0.0.0'])
  }

  /** Create a job with values given in a hash map
   *  @param params A hash map that contains the name
   *                and version of the job.
   */
  public SimpleJob(HashMap params) {
    newJob(params);
  }
 
  /** Execute the job and return the result. */
  public ResultSet execute(ParamFormValue params) {
    def p = (this.params?.getParamFormValue())?:new ParamFormValue();
    p.setParamValues(params)
    queryEngine.execute(p)
  }
 
  /** Execute the job and return the result. */
  public ResultSet execute(HashMap params) {
    if (!this.params) {
      this.params = new ParamForm()
    }
    def p = this.params.getParamFormValue().setParamValues(params)
    queryEngine.execute(p)
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
    new SimpleJobInstance(this).getOutputFormats()
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param outputFormat This tells us what kind of output you want to create,
   *                      For example, you might want HTML or CSV.
   *  @param paramFormValue An object that gives us the parameters to be used 
   *                        when creating the output for the job.
   *  @param out       An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, 
              Writer out) {
    new SimpleJobInstance(this).run(outputFormat, paramFormValue, out)
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param outputFormat This tells us what kind of output you want to create,
   *                      For example, you might want HTML or CSV.
   *  @param paramFormValue An object that gives us the parameters to be used 
   *                        when creating the output for the job.
   *  @param out       An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, 
              OutputStream out) {
    new SimpleJobInstance(this).run(outputFormat, paramFormValue, out)
  }

  /** Is this object valid and ready to run? */
  boolean validate() {
    return false
  }

}
