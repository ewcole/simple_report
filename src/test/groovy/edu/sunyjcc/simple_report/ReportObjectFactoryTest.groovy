package edu.sunyjcc.simple_report


/** Test the ReportObjectFactory class. */
public class ReportObjectFactoryTest extends GroovyTestCase {

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
    "******** $s ********************"
  }

  void testCacheCreation() {
    String type = 'param';
    String name = 'scoobydoo';
    println "Running test: type:'$type', name: '$name'";
    def fsf = getFileSourceFactory()
    assert fsf
    def rof = getReportObjectFactory()
    assert rof
    def src = fsf.getSource(type, name)
    assert src.size()
    def cache = rof.cache;
    println "cache = $cache"
    assert cache
    def c = cache[type]
    println "c=$c"
    assert c && c.getSrc
    println "c.keySet() == ${c.keySet()}"
    def getSrc = c.getSrc
    println "getSrc is a ${getSrc.getClass()}"
    assert getSrc
    println "getSrc=${getSrc.toString()}"
    assert getSrc
    String rofSrc = getSrc(name)
    println "rofSrc = $rofSrc"
    assert src == rofSrc
  }

  def runTest(String type, String name) {
    println "Running test: type:'$type', name: '$name'"
    def fsf = getFileSourceFactory()
    assert fsf
    def rof = getReportObjectFactory()
    assert rof
    def src = fsf.getSource(type, name)
    println "fsfSrc = $src"
    assert src.size()
    def cache = rof.cache;
    assert cache
    def c = cache[type]
    assert c && c.getSrc
    def getSrc = c.getSrc
    assert getSrc
    assert getSrc
    String rofSrc = getSrc(name)
    println "rofSrc = $rofSrc"
    assert src == rofSrc
  }

  void testGetParamSource() {
    printBanner("testGetParamSource");
    runTest('param', 'scoobydoo')
  }

  void testGetParamFormSource() {
    printBanner("testGetParamFormSource")
    runTest('param_form', "SubjectAndTerm");
  }

  void testGetParamFormSource2() {
    printBanner("testGetParamFormSource2")
    runTest('paramForm', "SubjectAndTerm");
  }
  void testGetJrxmlSource() {
    printBanner("testGetJrxmlSource")
    runTest('jrxml', "apps")
  }
}