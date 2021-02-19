package edu.sunyjcc.simple_report

/** Import this so that we can call this easily in web applications */
import javax.servlet.ServletResponse;

/** This class is responsible for managing the state of a report request.  It
 *  validates the parameter form and executes the target application.
 */
public class Invocation implements Exportable, Runnable {
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
  
  void setParamValues(pvals) {
    params.setParamValues(pvals)
  }

  SystemParams getSystemParams() {
    this.factory.clientEnv.systemParams
  }
  
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
      this.params = target.getParamFormValue()
    }
    target.init(args);
    params.init(args);
    return this
  }

  /** Return a HashMap with info about this Invocation. */
  def export() {
    [type: reportObjectType,
     name: name,
     isValid: isValid,
     params: params.export(),
     target: target.export()]
  }
  
  /** Create a new invocation object with the type and name given.  Generally,
   *  You would not call this directly. */
  public Invocation(ReportObjectFactory factory,
                    String reportObjectType,
                    String name) {
    this.factory          = factory;
    this.reportObjectType = reportObjectType;
    this.name             = name;
    this.init()
  }

  // Methods required by Runnable interface
  
  /** Get a param form value for the object.*/
  @Override
  ParamFormValue getParamFormValue() {
    params
  }

  /** Get a list of the supported output types */
  ArrayList<OutputFormat> getOutputFormats() {
    // Just JSON 
    target.getOutputFormats()
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  @Override
  boolean run(OutputFormat outputFormat, 
              ParamFormValue paramFormValue, 
              Writer out) {
    target.run(outputFormat, paramFormValue, out)
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, Writer out) {
    target.run(outputFormat, params, out)
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, OutputStream out) {
    target.run(outputFormat, params, out)
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, ServletResponse response) {
    if (outputFormat.isBinary) {
      // Get an outputStream
        def b = new ByteArrayOutputStream()
        target.run(outputFormat, params, b)
        // We've made it this far; now set the mime type.
        response.getOutputStream() << b.toByteArray()
    } else {
      target.run(outputFormat, params, response.getWriter())
    }
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  @Override
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, OutputStream out) {
    // println "In Invocation.run($outputFormat)"
    // def writer = new BufferedWriter(new OutputStreamWriter(out));
    target.run(outputFormat, paramFormValue, out)
  }

  /** Is this object valid and ready to run? */
  @Override boolean validate() {
    //assert params;
    isValid = target.getParamFormValue().validate()
    return isValid
  }

  String getSource() {
    return target.source
  }
}
