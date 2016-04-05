package edu.sunyjcc.simple_report;

import groovy.xml.*

/** A job that can be defined in a small Groovy script, producing an
 *  output
 */
public class SimpleJobInstance implements Exportable, Runnable {
  /** A SimpleJob object*/
  SimpleJob job;

  /** The values of all parameters */
  ParamFormValue params;

  // Methods Required by Runnable interface

  /** Get a param form value for the object.*/
  ParamFormValue getParamFormValue() {
    params
  }

  /** Create a new SimpleJobInstance for the job object.
   *  @param job The SimpleJob that this instance will wrap.
   */
  public SimpleJobInstance(SimpleJob job) {
    this.job = job
    this.params = job.getParamFormValue()
  }

  def runFunctions = [
    // JSON: {
    //   Writer out ->
    //     out.print(this.toJson());
    //     out.flush()
    //     return true;
    // },
    // 
    // Forget about CSV output for now.
    //
    // CSV: {
    //   Writer out, ResultSet resultSet ->
    //     //println "In SimpleJobInstance.runFunctions[CSV]()"
    //     def cols = resultSet?.columns?.list().collect {it.name}
    //     def columnHeaders = cols.collect {
    //       csvEscape(it)
    //     }
    //     out.println (columnHeaders.join(','))
    //     // Now print the data rows
    //     resultSet?.rows?.each {
    //       row ->
    //         out.println (
    //           cols.collect {
    //             val ->
    //               def v = convColumnType(row[val])?:''
    //               csvEscape("${(v)?:''}")
    //           }.join(','));
    //     }
    //     out.flush()
    //     return true;
    // },
    HTML: {
      Writer out ->
        //println "In SimpleJobInstance.runFunctions[HTML]()"
        def m = new MarkupBuilder(out)
        m.useDoubleQuotes = true
        m.html {
          head {
            title(this.job.title)
          }
          body {
            div(class: "simple_job $job.name") { 
              h1(this.job.title);
              div (class: "job_output") {
                 job.execute(paramFormValue, m)
              }
            }
          }
        }
        out.flush()
        return true;
    },
  ]
  

  /** Get a list of the supported output types */
  @Override
  ArrayList<OutputFormat> getOutputFormats() {
    runFunctions.keySet().collect {OutputFormat[it.toLowerCase()]}
  }

  /** Run the runnable object, writing its output data to the stream you
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  @Override
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue,
              Writer out) {
    def oFm = outputFormat.code
    this.paramFormValue.setParamValues(paramFormValue)
    assert runFunctions[oFm]
    if (runFunctions[oFm]) {
      return runFunctions[oFm](out);
    }
    return false
  }

  /** Run the runnable object, writing its output data to the stream you
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  @Override
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue,
              OutputStream out) {
    run(outputFormat, paramFormValue, new BufferedWriter(new OutputStreamWriter(out)));
  }

  /** Run the runnable object, writing its output data to the stream you
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, Writer out) {
    run(outputFormat, this.params, out)
  }

  /** Is this object valid and ready to run? */
  @Override
  boolean validate() {
    params.validate()
  }

  /** Return a simple Groovy object with all the important information about
   *  the implementing object.
   */
  @Override
  def export() {
    def r = job.export()
  }

}
