package edu.sunyjcc.simple_report

/** Objects that implement this interface will have methods that return the 
 *  source code for various objects defined in the simple_reports library.
 */
public class FileSourceFactory extends SourceFactory {

  File sourceRoot;

  /** Return the source code for a Jasper Report object.  The source
   *  code will be the absolute pathname of the JRXML file.
   *  @param objType The type of object requested, always 'jrxml'
   *  @param objName The name of the report.
   *  @return A string containing the full pathname of the report 
   *          definition file.
   */
  public String getJasperSourceText(String objType, String objName) {
    assert sourceRoot
    def sourceDir = new File(sourceRoot, objType)
    assert sourceDir.exists()
    String fileName = (objName =~ /\.jrxml$/)?objName:"${objName}.jrxml";
    File jr = new File(sourceDir, fileName);
    return "${jr.canonicalFile}";
  }

  /** {@inheritDoc} */
  public String getSourceText(String objType, String objName) {
    assert sourceRoot
    def sourceDir = new File(sourceRoot, objType)
    assert sourceDir.exists()
    if (objType != 'jrxml') {
      def s = new File(sourceDir, "${objName}")
      if (!s.exists()) {
        throw new BuildException("No source code for $objType/$objName.");
      }
      return s.text
    } else {
      // return a reference to the file.
      getJasperSourceText(objType, objName);
    }
  }

  /** List the objects this factory can find the source for */
  public ArrayList list() {
    ['param',
     'param_form',
     'report', 
     'sql',
     'job',
     'jrxml'].collect {
      dir ->
        new File(sourceRoot, dir).list().grep{it =~ /(?i)\.[a-z]+?$/}.collect {
          [type: dir, name: it.replaceAll(/(?i)\.[a-z]+?$/, '')]
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
