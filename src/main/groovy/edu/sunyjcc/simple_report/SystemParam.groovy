package edu.sunyjcc.simple_report;

import groovy.json.*

/** A system parameter.  The value is provided by executing a closure provided
 *  by the client application
 */
public class SystemParam implements Exportable {

  ClientEnv clientEnv

  String name;
  String label;
  String description;

  Closure valueClosure;

  /** Perform whatever initialization is needed for this parameter. */
  Param init(HashMap args) {
    getListOfValues()?.init(args)
    return this
  }

  /** Return the values as a HashMap. */
  def export() {
    [name:         this.getName(),
     type:         'SYSTEM',
     description:  this.getDescription(),
     label:        this.getLabel(),
     value:        this.value,
    ]
  }

  def getValue() {
    if (valueClosure) {
      valueClosure();
    } else {
      null;
    }
  }

  def setValue(Object value) {
    this.valueClosure = {-> value}
  }
  
  /** Create a Param with the same value as this SystemParam */
  Param getParam() {
    def p = new Param();
    p.reportObjectFactory = clientEnv?.reportObjectFactory;
    p.name = this.name;
    p.type = ParamType.system;
    p.description = this.description;
    p.label = this.label;
    def value = this.valueClosure?this.valueClosure():null;
    p.defaultValue = value;
    return p;
  }

  /** Get a ParamValue from this SystemParam */
  ParamValue getParamValue() {
    def p = this.getParam();
    def pv = new ParamValue(p);
    pv.currentValue = this.valueClosure?this.valueClosure():null;
    return pv;
  }

  /** Get a ParamValue of this parameter type 
   *  @value The value for the ParamValue.
   */
  ParamValue getParamValue(def value) {
    return new ParamValue(this, type.convert(value))
  }

  public String toJson() {
    def j = new JsonBuilder(this.export())
  }
  
}
