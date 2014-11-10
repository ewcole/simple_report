package edu.sunyjcc.simple_report

/** A Client class that can be used within a Plain Old Groovy Object. It has no 
 *  interaction with the environment, and is probably mostly useful for testing. */
public class PogoClient extends Client {

  /** Initialize the Client object and all composed objects */
  public Client init(HashMap args) {
    return super.init(args)
  }

  /** Present a single parameter as it should appear in a parameter form
   *  @param ParamValue The parameter value we want to collect
   */
  public showParam(ParamValue paramValue) {
    return paramValue.export()
  }

  /** Show all of the parameters in the parameter form and collect their values
   *  @param ParamFormValue The parameter value we want to collect
   */
  public showParamForm(ParamFormValue paramFormValue) {
    return paramFormValue.export()
  }

  // /** Zero-argument constructor */
  // public PogoClient() {
  // }
 
  // /** Constructor with ReportObjectFactory argument */
  // public PogoClient(ReportObjectFactory factory) {
  //   setReportObjectFactory(factory);
  // }

}