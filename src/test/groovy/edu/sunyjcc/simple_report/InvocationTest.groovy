package edu.sunyjcc.simple_report

public class InvocationTest extends GroovyTestCase {
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

  void testGetInvocation() {
    def rf = getReportObjectFactory();
    assert rf
    def i = rf.getInvocation('param', 'scoobydoo')
    assert i
    assert i instanceof Invocation
  }

  void testExport() {
    def rf = getReportObjectFactory();
    assert rf
    def i = rf.getInvocation('paramForm', 'SubjectAndTerm')
    assert i
    assert i instanceof Invocation
    def ie = i.export()
    println "i.export()==${ie}"
    def sampleData = [type: "paramForm",
                      name: "SubjectAndTerm",
                      isValid: false]
    sampleData.each {
      k, v ->
        assert ie[k] == v
    }
    // assert ie == [type: "paramForm",
    //               name: "SubjectAndTerm",
    //               isValid: false,
    //               params:  'null'
    //              ]
  }
}
