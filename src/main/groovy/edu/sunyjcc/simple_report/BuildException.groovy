package edu.sunyjcc.simple_report

public class BuildException extends Exception {

  /** One-argument constructor that sends the error message.
   *  @param message The error message
   */
  public BuildException(String message) {
    super(message);
  }
  // /** */
  // def parent
  // /** Useful and informative information about this error. */
  // def data

  /** The source code for the object being built, if available.  */
  String source;

}