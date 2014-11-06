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
  private boolean isValid = false;

  /** Create a new invocation object with the type and name given.  Generally,
   *  You would not call this directly. */
  public Invocation(ReportObjectFactory factory,
                    String reportObjectType,
                    String name) {
    this.factory          = factory;
    this.reportObjectType = reportObjectType;
    this.name             = name;
  }

  /** Property accessor for isValid */
  boolean getIsValid() {
    isValid;
  }

  /** Property setter for isValid; don't allow changes. 
   *  @param x This is simply ignored. 
   */
  boolean setIsValid(boolean x) {
    // Don't allow updates from the outside
  }

  public Invocation init(HashMap args) {
    factory?.init(args);
    paramForm?.init(args);
  }

  public Invocation validate() {

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
