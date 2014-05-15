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

  /** The current value for the parameter */
  def value

  /** Set the value of the parameter, making sure it is of the right type. 
   *  @param value The new value for the parameter
   */
  def setValue(def value) {
    assert this.type
    //assert value instanceof this.type
    
  }

  /** Return the values as a HashMap. */
  def export() {
    [name:        this.name,
     type:        this.type.name,
     description: this.description,
     label:       this.label,
     value:       this.value]
  }

  /** Explicit-argument constructor */
  public Param(String name, 
               Class type, 
               String description, 
               String label) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.label = label;
  }
}
