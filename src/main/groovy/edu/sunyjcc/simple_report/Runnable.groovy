package edu.sunyjcc.simple_report;

/** This can be run by an Invocation object.
 */
public interface Runnable {
  /** Get a param form value for the object.*/
  ParamFormValue getParamFormValue();

  /** Get a list of the supported output types */
  ArrayList<OutputFormat> getOutputFormats();

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, Writer out);

  /** Is this object valid and ready to run? */
  boolean validate();
}