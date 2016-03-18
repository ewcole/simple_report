package edu.sunyjcc.simple_report;

import groovy.json.*

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
    println "setting Object value"
    if (v && v instanceof String) {
      println "parsing String";
      this.currentValue = param.type.parse(v);
    } else {
      println "Copying value";
      this.currentValue = v;
    }
  }

  /** Set the current value of the parameter 
   *  @param v The value you would like to assign.
   */
  void setValue(String v) {
    assert param.type;
    println "setting String value"
    if (v) {
      currentValue = param.type.parse(v);
    } else {
      currentValue = null;
    }
  }

  void setCurrentValue(def v) {
    setValue(v);
  }

  /** Set the current value of the parameter 
   *  @param v The value you would like to assign.
   */
  void setValue(ParamValue v) {
    currentValue = v.currentValue;
  }

  /** Constructor with no current value */
  public ParamValue(Param param) {
    this.param = param;
    setValue param.defaultValue;
  }
  
  /** Constructor with a current value */
  public ParamValue(Param param, Object currentValue) {
    this.param = param;
    setValue currentValue;
  }

  /** Clone a parameter value from another parameter value. */
  public ParamValue(ParamValue pv) {
    this.param = pv.param;
    setValue pv.currentValue;
  }

  /** Export to a HashMap */
  def export() {
    def m = param.export();
    m.value = this.currentValue;
    m;
  }

  boolean isValid() {
    
  }

  ParamValue init(HashMap args = [:]) {
    param.init(args);
    return this;
  }

  String toJson() {
    //def j = new JsonOutput()
    //j.toJson(this.export())
    def e = this.export()
    '{' + e.collect {
      key, value ->
        "\"$key\": \"$value\""
    }.join(', ') + '}'
  }
}
