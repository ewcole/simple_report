package edu.sunyjcc.simple_report

/**
 *
 */
public class Invocation implements Exportable {
  /** Our source for all objects */
  ReportObjectFactory  factory
  /** A string representing the type of object we are trying to run. */
  String            reportObjectType
  /** The name of the object we are trying to run. */
  String            name
  /** A unique name (within the factory) that identifies this invocation. */
  String id;
  /** A parameter form to hold our values */
  ParamFormValue    params;
  boolean isValid = false;

  /** Create a new invocation object with the type and name given.  Generally,
   *  You would not call this directly. */
  public Invocation(ReportObjectFactory factory,
                    String reportObjectType,
                    String name) {
    this.factory          = factory;
    this.reportObjectType = reportObjectType;
    this.name             = name;
  }


  /** Return a HashMap with info about this Invocation. */
  def export() {
    [type: reportObjectType,
     name: name,
     id:   id,
     isValid: isValid,
     params: params.export()]
  }
}
