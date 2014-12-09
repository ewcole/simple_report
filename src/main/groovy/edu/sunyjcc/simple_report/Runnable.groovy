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
   *  @param outputFormat This tells us what kind of output you want to create,
   *                      For example, you might want HTML or CSV.
   *  @param paramFormValue An object that gives us the parameters to be used 
   *                        when creating the output for the report.
   *  @param out       An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, 
              Writer out);

  /** Is this object valid and ready to run? */
  boolean validate();
}