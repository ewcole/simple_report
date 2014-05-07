package edu.sunyjcc.simple_report;

/** A parameter definition to be used in a report or application invocation.
 */
public class SimpleReportParam {
  /** The parameter name */
  String name;

  /** The type of the parameter */
  Class type;
  
  /** A one-line description of the parameter */
  String description;
  
  /** A label to be displayed when prompting for the parameter*/
  String label;

  /** Explicit-argument constructor */
  public SimpleReportParam(String name, 
                           Class type, 
                           String description, 
                           String label) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.label = label;
  }
}
