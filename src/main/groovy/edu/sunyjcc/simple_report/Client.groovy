package edu.sunyjcc.simple_report

/** Client objects will provide the interface between a runtime environment
 *  And the ReportObjectFactory.  It is responsible for presenting the parameter
 *  forms, running report objects, and delivering the output to the user.
 */
public abstract class Client {
  /** The source for all objects we will use. */
  ReportObjectFactory factory;

  /** Set the value of the factory property. */
  public void setReportObjectFactory(ReportObjectFactory factory) {
    this.factory = factory
  }

  /** Initialize the Client object and all composed objects */
  public abstract Client init(HashMap args);

  /** Present a single parameter as it should appear in a parameter form
   *  @param ParamValue The parameter value we want to collect
   */
  public abstract showParam(ParamValue paramValue);

  /** Present a single parameter as it should appear in a parameter form
   *  @param ParamFormValue The parameter value we want to collect
   */
  public abstract showParamForm(ParamFormValue paramFormValue);


}