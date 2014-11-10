package edu.sunyjcc.simple_report

public class ClientTest extends GroovyTestCase {
    /** Get the source directory for the parameter forms, reports, etc. */
  File getSourceDir() {
    File f = new File('src/samples/dir1');
  }

  /** Get a subdirectory of the source directory */
  File getSourceSubDir(String subDirName) {
    File f = new File(getSourceDir(), subDirName);
  }

  /** Get a FileSourceFactory with the source directory as its root. */
  FileSourceFactory getFileSourceFactory() {
    def fsf = new FileSourceFactory(getSourceDir())
    assert fsf
    return fsf
  }

  ReportObjectFactory getReportObjectFactory() {
    def fsf = getFileSourceFactory();
    def rof = new ReportObjectFactory(fsf);
    assert rof;
    assert rof.cache
    rof;
  }

  void printBanner(String s) {
    println ""
    println ""
    println ("*" * 79)
    println "******** $s ********************"
    println ("*" * 79)
  }

  void testCreateClient() {
    printBanner "testGetClient";
    def factory = getReportObjectFactory();
    //def c = new PogoClient()
  }

  
}