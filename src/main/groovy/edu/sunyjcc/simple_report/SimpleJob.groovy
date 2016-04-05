package edu.sunyjcc.simple_report;

import groovy.sql.Sql;
import groovy.xml.*;

/** A job that can be defined in a small Groovy script, producing an 
 *  output
 */
public class SimpleJob implements Exportable, Buildable, Runnable {

  void debug(String text) {
    println text;
  }

  String getBuildDocHtml() {
    ("This creates a job that can change data in the database.  "
     + "")
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [name:    [desc: "The name of the job.  It defaults to the name of the script."],
     version: [desc: "The version number for this job."],
     title:   [desc: "The job's title."],
     jobEngine:  [desc:  "A Closure that does the work"]]
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

  /** Replace the parameter form for this job */
  public setParams(ParamForm params) {
    this.params = params;
  }

  public ParamForm getParams() {
    if (!params) {
      params = new ParamForm()
    }
    params;
  }

  /** The engine that will power the job */
  private Closure jobEngine

  /* End of job-level properties *************/

  /** A database connection to be used during execution */
  Sql sql;

  /** The factory that produced this object. */
  ReportObjectFactory factory;

  public void setJobParams(ArrayList l) {
    // Don't let outsiders mess with params.
  }

  SimpleJob init(HashMap args) {
    assert jobEngine
    // if (queryEngine) {
    //   queryEngine.init(args)
    // }
    if (args.containsKey('sql')) {
      this.sql = args.sql
    }
    if (params) {
      params.init(args)
    }
    return this
  }

  /** Return a HashMap representing this job object. */
  def export() {
    [name: this.name,
     type: 'SimpleJob',
     title: this.title,
     version: this.version,
     description: this.description,
     params: this.params?.export(),
     //queryEngine: this.queryEngine?.export(),
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
      this.title = params.title?:params.name;
    }
    if (params.jobEngine) {
      this.jobEngine = jobEngine
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
 
  public void runJobEngine(HashMap params, MarkupBuilder markupBuilder) {
    assert jobEngine;
    // 1. Build the delegate that defines the classes and 
    //    properties the closure can call
    def model = new Expando(sql: sql);
    model.params = params?:[:]
    model.markupBuilder = markupBuilder
    model.factory = this.factory
    // 2. Call the closure with the pre-defined environment
    debug "model=$model"
    jobEngine.delegate = model;
    jobEngine.resolveStrategy = Closure.DELEGATE_ONLY
    jobEngine();
  }


  /** Execute the job and return the result. */
  public void execute(ParamFormValue params, MarkupBuilder m) {
    debug "In execute(${params.export()}, $m)"
    def p = (this.params?.getParamFormValue())?:params;
    debug "p=${p.export()}"
    p.setParamValues(params)
    debug "After p.setParamValues: p=$p"
    def vals = p.getValues()?.inject([:]) {
      map, pv ->
        map[pv.key] = pv.value.value;
        map;
    }
    debug "vals=$vals";
    execute(vals, m);
  }
 
  /** Execute the job and return the result. */
  public void execute(HashMap params, MarkupBuilder m) {
    runJobEngine(params, m)
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
