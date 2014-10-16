package edu.sunyjcc.simple_report

/** Objects that implement this interface will have methods that return the 
 *  source code for various objects defined in the simple_reports library.
 *  SourceFactory objects are not responsible for caching; that will be done 
 *  elsewhere.
 */
public abstract class SourceFactory {

  /** Go to the source
   *  @param objType The kind of object that we are trying to get
   *  @param objName The name of the object.
   */
  abstract String getSourceText(String objType, String objName);

  /** Get the source for a parameter object */
  String getParamSource(String name) {
    def pf = getSourceText("param", "${name}.groovy")
  }

  /** Get the source for a parameter form object */
  public String getParamFormSource(String name) {
    def pf = getSourceText("param_form", "${name}.groovy")
  }

  /** Get a .jrxml file, defining a Jasper Report. */
  public String getJrxmlSource(String name) {
    def pf = getSourceText("jrxml", "${name}.jrxml")
  }
  
  /** Get the source for an object of the desired type 
   *  @param type   The kind of object; one of 'param', 'paramForm', 'jrxml'.
   */
  public String getSource(String type, String name) {
    def ext = (type == 'jrxml')?'jrxml':'groovy';
    def qName = "${name}.${ext}"
    getSourceText(type, qName)
  }

}
