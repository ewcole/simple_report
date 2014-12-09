package edu.sunyjcc.simple_report;

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

  /** Get a list of the supported output types */
  @Override
  ArrayList<OutputFormat> getOutputFormats() {
    return []
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  @Override
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, Writer out) {

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