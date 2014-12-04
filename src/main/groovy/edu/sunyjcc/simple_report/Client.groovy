package edu.sunyjcc.simple_report

/** Client objects will provide the interface between a runtime environment
 *  And the ReportObjectFactory.  It is responsible for presenting the parameter
 *  forms, running report objects, and delivering the output to the user.
 */
public abstract class Client {
  /** The source for all objects we will use. */
  ReportObjectFactory factory;

  /** Set the value of the factory property. */
  public Client setReportObjectFactory(ReportObjectFactory factory) {
    this.factory = factory
    return this
  }

  public Client() {

  }

  public Client(ReportObjectFactory factory) { 
    setReportObjectFactory(this)
  }

  /** Initialize the Client object and all composed objects */
  public Client init(HashMap args) { return this }

  /** Present a single parameter as it should appear in a parameter form
   *  @param ParamValue The parameter value we want to collect
   */
  public abstract showParam(ParamValue paramValue);

  /** Present a single parameter as it should appear in a parameter form
   *  @param invocation An Invocation (which has a param form)
   */
  public showParam(Invocation invocation, String paramName) {
    showParam(invocation.params[paramName]);
  }

  /** Show all of the parameters in the parameter form and collect their values
   *  @param ParamFormValue The parameter value we want to collect
   */
  public abstract showParamForm(ParamFormValue paramFormValue);

  /** Show all of the parameters in the parameter form and collect their values
   *  @param invocation An Invocation object.
   */
  public showParamForm(Invocation invocation) {
    showParamForm(invocation.params);
  }

  /** Validate the parameters for an invocation and run it
   *  @param invocation An Invocation representing the object we need to run.
   */
  public def run(Invocation invocation) {
    if (invocation.validate()) {
      [status: 'Run', invocation: invocation, result: invocation.run()]
    } else {
      [status: 'Errors', invocation: invocation]
    }
  }
}