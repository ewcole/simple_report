package edu.sunyjcc.simple_report

/** Objects that implement this interface will have methods that return the 
 *  source code for various objects defined in the simple_reports library.
 */
public class FileSourceFactory extends BaseSourceFactory {

  File sourceRoot;

  /** {@inheritDoc} */
  public String getSourceText(String objType, String objName) {
    def f = new File(sourceRoot, objType)
    assert f.exists()
    def s = new File(f, "${objName}")
    s.text
  }

  /** Public constructor that takes a root directory.
   *  @param sourceRoot The root directory of the source repository.
   */
  public FileSourceFactory(File sourceRoot) {
    this.sourceRoot = sourceRoot
  }
}
