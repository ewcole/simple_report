package edu.sunyjcc.simple_report

/** Objects that implement this interface will have methods that return the 
 *  source code for various objects defined in the simple_reports library.
 */
public abstract class BaseSourceFactory implements SourceFactory {

  File sourceRoot;

  /** Go to the source
   *  @param objType The kind of object that we are trying to get
   *  @param objName The name of the object.
   */
  abstract String getSourceText(String objType, String objName);

  /**  */
  @Override
  String getParamSource(String name) {
    def pf = getSourceText("param", "${name}.groovy")
  }

  /** Get the source for a parameter form object */
  @Override public String getParamFormSource(String name) {
    def pf = getSourceText("param_form", "${name}.groovy")
  }

  /** Get a .jrxml file, defining a Jasper Report. */
  @Override public String getJrxmlSource(String name) {
    def pf = getSourceText("jrxml", "${name}.jrxml")
  }
  
  /**
   * {@inheritDoc}
   *
   */
  @Override public String getSource(String type, String name) {
    def ext = (type == 'jrxml')?'jrxml':'groovy';
    def qName = "${name}.${ext}"
    getSourceText(type, qName)
  }

}
