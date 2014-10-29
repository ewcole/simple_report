package edu.sunyjcc.simple_report

/** Test the ParamFormValue class. */
public class ParamFormValueTest extends GroovyTestCase {

  void printBanner(String s) {
    println ""
    println ""
    println ("*" * 79)
    println "******** $s ********************"
    println ("*" * 79)
  }

  /** Get the source directory for the parameter forms, reports, etc. */
  File getSourceDir() {
    println 'Before Getting Source Dir'
    File f = new File('src/samples/dir1');
    println 'After Getting Source Dir'
    assert f
    assert f.exists()
  }

  /** Get a subdirectory of the source directory */
  File getSourceSubDir(String subDirName) {
    File f = new File(getSourceDir(), subDirName);
    assert f.exists()
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

  void getParamForm(String name) {
    getReportObjectFactory().getParamForm(name)
  }

  void testGetParamForm() {
    printBanner("testGetParamForm")
    def f = getParamForm("SubjectAndTerm");
    assert f;
    assert f.export()  == [subject: [name:        "subject", 
                                     type:        "STRING", 
                                     description: "subject", 
                                     label:       "subject", 
                                     default:     "ART"], 
                           term_code:[name:        "term_code", 
                                      type:        "STRING", 
                                      description: "term_code", 
                                      label:       "term_code", 
                                      default:     "201312"]]
  }

  void testGetParamFormValue() {
    printBanner("testGetParamFormValue")
    def f = getParamForm("SubjectAndTerm");
    assert f
    assert f.export() == [subject: [name:        "subject", 
                                    type:        "STRING", 
                                    description: "subject", 
                                    label:       "subject", 
                                    default:     "ART"], 
                          term_code:[name:        "term_code", 
                                     type:        "STRING", 
                                     description: "term_code", 
                                     label:       "term_code", 
                                     default:     "201312"]]
    def v = new ParamFormValue(f)
    assert v
  }

}