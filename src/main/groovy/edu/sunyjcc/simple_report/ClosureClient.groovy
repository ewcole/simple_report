package edu.sunyjcc.simple_report

/** A Client object that calls closures from its required functions.
 */
public class ClosureClient extends Client {

  /** A closure that shows one parameter */
  Closure showParamClosure;

  /** A closure that shows a parameter form */
  Closure showParamFormClosure;

  /** Present a single parameter as it should appear in a parameter form
   *  @param ParamValue The parameter value we want to collect
   */
  public showParam(ParamValue paramValue) {
    assert showParamClosure;
    showParamClosure(paramValue);
  }

  /** Show all of the parameters in the parameter form and collect their values
   *  @param ParamFormValue The parameter value we want to collect
   */
  public showParamForm(ParamFormValue paramFormValue) {
    assert showParamFormClosure;
    showParamFormClosure(paramFormValue);
  }

  /** 
   *  @param factory              A ReportObjectFactory 
   *  @param showParamClosure     A closure that shows one parameter
   *  @param showParamFormClosure A closure that shows a parameter form
   */
  public ClosureClient(ReportObjectFactory factory,
                       Closure showParamClosure,
                       Closure showParamFormClosure) {
    setReportObjectFactory(factory)
    this.showParamClosure = showParamClosure
    this.showParamFormClosure = showParamFormClosure
  }

}