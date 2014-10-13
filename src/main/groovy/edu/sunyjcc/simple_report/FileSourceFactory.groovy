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
    def f = new File(sourceRoot, objType)
    assert f.exists()
    def s = new File(f, "${objName}")
    s.text
  }

  /** Get the source for a parameter object */
  @Override
  String getParamSource(String name) {
    def pf = getSource("param", "${name}.groovy")
  }

  /** Get the source for a parameter form object */
  @Override public String getParamFormSource(String name) {
    def pf = getSource("param_form", "${name}.groovy")
  }

  /** Get a .jrxml file, defining a Jasper Report. */
  @Override public String getJrxmlSource(String name) {
    def pf = getSource("jrxml", "${name}.jrxml")
  }

  /** Public constructor that takes a root directory.
   *  @param sourceRoot The root directory of the source repository.
   */
  public FileSourceFactory(File sourceRoot) {
    this.sourceRoot = sourceRoot
  }
}
