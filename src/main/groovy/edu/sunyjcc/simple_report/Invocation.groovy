package edu.sunyjcc.simple_report

/** This class is responsible for managing the state of a report request.  It
 *  validates the parameter form and executes the target application.
 */
public class Invocation implements Exportable {
  /** Our source for all objects */
  ReportObjectFactory  factory

  /** A string representing the type of object we are trying to run. */
  String               reportObjectType

  /** The name of the object we are trying to run. */
  String              name

  // /** The front end */
  // Client              client;

  // /** A unique name (within the factory) that identifies this invocation. */
  // String              id;

  /** A parameter form to hold our values */
  ParamFormValue    params;

  /** The current state of the Invocation parameters */
  private boolean isValid = false;

  private Runnable target

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

  /** Initialize this object, using the parameters provided.
   *  Create the report object if necessary, and return this
   *  so that it can be chained.
   */
  public Invocation init(HashMap args = [:]) {
    // Don't initialize the factory.  That should already be done.
    // factory?.init(args);
    if (!target) {
      target = factory.getReportObject(reportObjectType, name)
      assert target instanceof Runnable
    }
    params = target.getParamFormValue()
    assert params
    params?.init(args);
    return this
  }

  @Override
  boolean validate() {
    assert params;
    isValid = params.validate()
    return isValid
  }

  /** Return a HashMap with info about this Invocation. */
  def export() {
    [type: reportObjectType,
     name: name,
     isValid: isValid,
     params: params?params.export(): [:]]
  }

  def run() {
    if (this.validate()) {
      target.run(params)
    }
  }
  
  /** Create a new invocation object with the type and name given.  Generally,
   *  You would not call this directly. */
  public Invocation(ReportObjectFactory factory,
                    String reportObjectType,
                    String name,
                    ParamForm params = null) {
    this.factory          = factory;
    this.reportObjectType = reportObjectType;
    this.name             = name;
    this.params           = params;
    this.init()
  }

}
