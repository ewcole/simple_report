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
    //println 'Before Getting Source Dir'
    File f = new File('src/samples/dir1');
    println "Source dir = $f"
    assert f
    assert f.exists()
    return f
  }

  /** Get a FileSourceFactory with the source directory as its root. */
  FileSourceFactory getFileSourceFactory() {
    def d = getSourceDir();
    assert d;
    def fsf = new FileSourceFactory(d)
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
    // def rof = getReportObjectFactory()
    // assert rof
    // rof.getParamForm(name)
    getReportObjectFactory().getParamForm(name);
  }

  void testGetParamForm() {
    printBanner("testGetParamForm")
    def f = getReportObjectFactory().getParamForm("SubjectAndTerm");
    assert f
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
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    assert pf.export() == [subject: [name:        "subject", 
                                    type:        "STRING", 
                                    description: "subject", 
                                    label:       "subject", 
                                    default:     "ART"], 
                          term_code:[name:        "term_code", 
                                     type:        "STRING", 
                                     description: "term_code", 
                                     label:       "term_code", 
                                     default:     "201312"]]
    def v = new ParamFormValue(pf)
    assert v
    println "v.term_code=${v.term_code}"
    assert v.term_code;
    println "v.term_code.export()=${v.term_code.export()}"
    println "v.subject=${v.subject}"
    println "v.subject.export()=${v.subject.export()}"
    // Should be same as the param form export, but with added values.
    assert v.export() == [subject: [name:        "subject", 
                                    type:        "STRING", 
                                    description: "subject", 
                                    label:       "subject", 
                                    "default":     "ART",
                                    value:         "ART"
                                   ], 
                          term_code:[name:        "term_code", 
                                     type:        "STRING", 
                                     description: "term_code", 
                                     label:       "term_code", 
                                     "default":     "201312",
                                     value:         "201312"]]
  }

  void testGetParamFormParamValue() {
    printBanner("testGetParamFormParamValue")
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    assert pf.export() == [subject: [name:        "subject", 
                                    type:        "STRING", 
                                    description: "subject", 
                                    label:       "subject", 
                                    default:     "ART"], 
                          term_code:[name:        "term_code", 
                                     type:        "STRING", 
                                     description: "term_code", 
                                     label:       "term_code", 
                                     default:     "201312"]]
    def v = new ParamFormValue(pf)
    assert v
    println "v.term_code=${v.term_code}"
    assert v.term_code;
    assert v.term_code.export() == [name:        "term_code", 
                                    type:        "STRING", 
                                    description: "term_code", 
                                    label:       "term_code", 
                                    "default":     "201312",
                                    value:         "201312"]
  }

 void testSetParamValue() {
    printBanner("testGetParamFormParamValue")
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    def v = new ParamFormValue(pf)
    assert v
    v.term_code.value = "201512"
    assert v.term_code.export() == [name:        "term_code", 
                                    type:        "STRING", 
                                    description: "term_code", 
                                    label:       "term_code", 
                                    "default":     "201312",
                                    value:         "201512"]
 }

}