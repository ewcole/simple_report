package edu.sunyjcc.simple_report;

/** 
 *  A list of the values for a ParameterForm.
 */
public class ParamFormValue implements Exportable {
  /** The form that tells us what parameters are required. */
  ParamForm paramForm;
  HashMap<String, Param> parameters

  public init
  public ParamFormValue(ParamForm paramForm) {
    this.paramForm = paramForm
  }

  @Override
  public def export() {
    [paramForm: paramForm.export(),
     values:    this.getValues()]
  }

  /** Return a HashMap with the keys being the names of the parameters
   *  and the values their current value.
   */
  public HashMap getValues() {
    parameters.inject([:]) {
      pMap, param ->
        pMap[param.key] = param.currentValue
        return pMap;
    }
  }
  
  public ParamForm init(HashMap args) {

  }
}