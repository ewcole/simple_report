package edu.sunyjcc.simple_report;

/** A parameter definition to be used in a report or application invocation.
 */
public class Param implements Exportable, Buildable {

  String getBuildDocHtml() {
    "Change me."
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {[:]}

  /** The parameter name */
  String name;

  /** If this is non-null, inherit unspecified properties from this paramter;
   *  Also, call the super-param's validators before our own validation.
   */
  Param superParam;

  /** The type of the parameter */
  ParamType type;
  
  /** A one-line description of the parameter */
  String description;
  
  /** A label to be displayed when prompting for the parameter*/
  String label;

  /** A default value for the parameter */
  Object defaultValue;

  /** Perform whatever initialization is needed for this parameter. */
  Param init(HashMap args) {
    return this
  }
  
  /** Return the values as a HashMap. */
  def export() {
    [name:        this.name,
     type:        this.type.desc,
     description: this.description,
     label:       this.label,
     'default':   this.defaultValue]
  }

  /** Explicit-argument constructor */
  public Param(String name, 
               ParamType type, 
               String description, 
               String label,
               Object defaultValue = null,
               Param superParam = null) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.label = label;
    this.defaultValue = defaultValue
    this.superParam = superParam;
  }

  boolean validate(def value) {
    return true
    // return type.convert(value);
  }

  /** Get a ParamValue of this parameter type */
  ParamValue getParamValue() {
    return new ParamValue(this)
  }

  /** Get a ParamValue of this parameter type 
   *  @value The value for the ParamValue.
   */
  ParamValue getParamValue(def value) {
    return new ParamValue(this, type.convert(value))
  }

}
