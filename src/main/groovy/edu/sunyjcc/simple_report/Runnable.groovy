package edu.sunyjcc.simple_report;

/** This can be run by an Invocation object.
 */
public interface Runnable {
  /** Get a param form value for the object.*/
  ParamFormValue getParamFormValue();

  // /** Get the mime type that this will produce */
  // String getMimeType() 

  // /** Run the runnable object, writing its output data to the stream you 
  //  *  provide.
  //  *  @param out An output stream that will hold the results of your run.
  //  */
  // void run(OutputStream out);

  /** Run the report, returning the results as a HashMap with keys mimeType, 
   *   
   */
  HashMap run(ParamFormValue paramFormValue);
}