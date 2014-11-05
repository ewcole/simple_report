package edu.sunyjcc.simple_report;

/** A value for a Parameter object.  They are separated so that we can share 
 *  Param objects  
 */
public class ParamValue implements Exportable {

  /** The parameter for which we are collecting values. */
  Param param;

  /** The current value of this parameter. */
  Object currentValue;

  /** Get the current value of the parameter */
  Object getValue() {
    currentValue
  }

  /** Set the current value of the parameter 
   *  @param v The value you would like to assign.
   */
  void setValue(def v) {
    currentValue = v;
  }
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
