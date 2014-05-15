package edu.sunyjcc.simple_report;

/** A parameter definition to be used in a report or application invocation.
 */
public class Param implements Exportable {
  /** The parameter name */
  String name;

  /** The type of the parameter */
  Class type;
  
  /** A one-line description of the parameter */
  String description;
  
  /** A label to be displayed when prompting for the parameter*/
  String label;

  /** A default value for the parameter */
  Object defaultValue;

  /** The current currentValue for the parameter */
  Object currentValue

  /** Set the currentValue of the parameter, making sure it is of the right type. 
   *  @param currentValue The new currentValue for the parameter
   */
  def setValue(def currentValue) {
    if (currentValue == null) {
      this.currentValue = null
      return null
    } else {
      assert this.type
      //assert currentValue instanceof this.type
    }
  }

  /** Return the values as a HashMap. */
  def export() {
    [name:        this.name,
     type:        this.type.name,
     description: this.description,
     label:       this.label,
     'default':   this.defaultValue,
     value:       this.currentValue]
  }

  /** Explicit-argument constructor */
  public Param(String name, 
               Class type, 
               String description, 
               String label,
               Object defaultValue = null) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.label = label;
    this.defaultValue = defaultValue
  }
}
