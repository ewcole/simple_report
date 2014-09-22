package edu.sunyjcc.simple_report

/** Objects that implement this interface will have methods that return the 
 *  source code for various objects defined in the simple_reports library.
 */
public class FileSourceFactory {

  File sourceRoot;

  /** 
   *  @param objType The kind of object that we are trying to get
   *  @param objName The name of the object.
   */
  private String getSource(String objType, String objName) {
    new File(sourceRoot, objType).text
  }

  /** Get the source for a parameter object */
  @Override
  String getParamSource(String name) {
    def pf = getSource("param", name)
  }

  /** Get the source for a parameter form object */
  @Override public String getParamFormSource(String name) {
  }

  /** Get a .jrxml file, defining a Jasper Report. */
  @Override public String getJrxmlSource(String name) {

  }

  /** Public constructor that builds on a 
   *  
   */
  public FileSourceFactory(File sourceRoot) {
    
  }
}
