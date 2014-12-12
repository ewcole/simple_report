package edu.sunyjcc.simple_report;

import groovy.xml.*

/** A report that can be defined in a small Groovy script, producing an
 *  output
 */
public class SimpleReportInstance implements Exportable, Runnable {
  /** A SimpleReport object*/
  SimpleReport report;

  /** The values of all parameters */
  ParamFormValue params;

  // Methods Required by Runnable interface

  /** Get a param form value for the object.*/
  ParamFormValue getParamFormValue() {
    params
  }

  /** Create a new SimpleReportInstance for the report object.
   *  @param report The SimpleReport that this instance will wrap.
   */
  public SimpleReportInstance(SimpleReport report) {
    this.report = report
    this.params = report.getParamFormValue()
  }

  def runFunctions = [
    // JSON: {
    //   Writer out ->
    //     out.print(this.toJson());
    //     out.flush()
    //     return true;
    // },
    HTML: {
      Writer out, ResultSet resultSet ->
        def m = new MarkupBuilder(out);
        m.setDoubleQuotes(true)
        m.table {
          thead {
            resultSet.columns.each {
              c ->
                th(class: "${c.name}", tooltip: "${c.description}",
                   "${c.label}");
            }
          }
          tbody {
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
    if (runFunctions[outputFormat]) {
      def rs = target.execute(paramFormValue)
      return runFunctions[outputFormat](out, rs);
    }
    return false
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
    def r = report.export()
  }

}