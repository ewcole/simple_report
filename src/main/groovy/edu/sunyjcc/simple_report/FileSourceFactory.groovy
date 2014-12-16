package edu.sunyjcc.simple_report

/** Objects that implement this interface will have methods that return the 
 *  source code for various objects defined in the simple_reports library.
 */
public class FileSourceFactory extends SourceFactory {

  File sourceRoot;

  /** {@inheritDoc} */
  public String getSourceText(String objType, String objName) {
    assert sourceRoot
    def sourceDir = new File(sourceRoot, objType)
    assert sourceDir.exists()
    def s = new File(sourceDir, "${objName}")
    assert s.exists()
    s.text
  }

  /** List the objects this factory can find the source for */
  public ArrayList list() {
    ['param',
     'param_form',
     'report'].collect {
      dir ->
        new File(sourceRoot, dir).list().grep{it =~ /(?i)\.groovy$/}.collect {
          [type: dir, name: it.replaceAll(/(?i)\.groovy$/, '')]
        }
    }.flatten() 
  }

  /** Public constructor that takes a root directory.
   *  @param sourceRoot The root directory of the source repository.
   */
  public FileSourceFactory(File sourceRoot) {
    this.sourceRoot = sourceRoot
  }

  /** Public constructor that takes a root directory.
   *  @param sourceRoot The name of the root directory of the source repository.
   */
  public FileSourceFactory(String sourceRoot) {
    this.sourceRoot = new File(sourceRoot);
  }

}
