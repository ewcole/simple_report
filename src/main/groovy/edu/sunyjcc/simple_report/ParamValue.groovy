package edu.sunyjcc.simple_report;

/** A value that can be held by 
 */
public class ParamValue implements Exportable {

  /** The parameter for which we are collecting values. */
  Param param;

  /** The current value of this parameter. */
  Object currentValue;

  /** Constructor with no current value */
  public ParamValue(Param param) {
    this.param = param;
    this.currentValue = param.defaultValue;
  }
  
  /** Constructor with a current value */
  public ParamValue(Param param, Object currentValue) {
    this.param = param;
    this.currentValue = currentValue;
  }

  /** Clone a parameter value from another parameter value. */
  public ParamValue(ParamValue pv) {
    this.param = pv.param;
    this.currentValue = pv.currentValue;
  }

  /** Export to a HashMap */
  def export() {
    def m = param.export();
    m.value = this.currentValue;
    m;
  }

  boolean isValid() {
    
  }

}
