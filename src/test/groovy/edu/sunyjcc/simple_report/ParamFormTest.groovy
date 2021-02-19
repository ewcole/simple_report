package edu.sunyjcc.simple_report
import groovy.json.*

/** Test the ParamForm class. */
public class ParamFormTest extends GroovyTestCase {

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
                                     label:       "Subject", 
                                     default:     "ART"], 
                           term_code:[name:        "term_code", 
                                      type:        "STRING", 
                                      description: "term_code", 
                                      label:       "Term Code", 
                                      default:     "201312"]]
  }

  void testAddParam() {
    printBanner "testAddParam"
    def f = getReportObjectFactory();
    assert f;
    def form = f.getParamForm("SubjectAndTerm");
    assert form;
    def p = f.build {
      param (name: "season");
    }
    assert p instanceof Param;
    form.addParam(p);
    assert form.params.keySet().collect {it} == ['term_code', 'subject', 'season']
    assert form.params.season == p
  }

  void testAddParams() {
    printBanner "testAddParams"
    def f = getReportObjectFactory();
    assert f;
    def stForm = f.getParamForm("SubjectAndTerm");
    assert stForm;
    def pForm = new ParamForm();
    pForm.copyFrom(stForm);
    assert pForm && pForm.export() == stForm.export()
  }

  void testPrototypeConstructor() {
    printBanner "testPrototypeConstructor"
    def f = getReportObjectFactory();
    assert f;
    def stForm = f.getParamForm("SubjectAndTerm");
    assert stForm;
    def pForm = new ParamForm(stForm);
    assert pForm && pForm.export() == stForm.export()
  }

  void testPrototype() {
    printBanner "testPrototype"
    def f = getReportObjectFactory()
    def pf = f.build {
      params(copyFrom: "SubjectAndTerm")
    }
    assert pf.params.keySet().collect{it} == ["term_code", "subject"]
    def st = f.getParamForm("SubjectAndTerm");
    assert st.export() == pf.export()
  }

  void testDuplicateParams() {
    printBanner "testDuplicateParams"
    def f = getReportObjectFactory()
    def pf = f.build {
      params(copyFrom: "SubjectAndTerm")
    }
    shouldFail(BuildException) {
      pf.addParam(f.build { param name: "term_code" });
    }
  }

  void testRefreshSystemParams() {
    printBanner "testRefreshSystemParams"
    def f = getReportObjectFactory()
    assert f?.clientEnv?.systemParams
    assert f.clientEnv;
    assert f.clientEnv.systemParams
    f.clientEnv.systemParams.dalek_phrase = "Exterminate!"
    assert f.clientEnv.systemParams.params.dalek_phrase instanceof SystemParam
    assert f.clientEnv.systemParams.keySet() == ['dalek_phrase']
    def pf = f.build {
      params(copyFrom: "SubjectAndTerm")
    }
    assert pf.reportObjectFactory == f
    pf.refreshSystemParams()
    def pfe = pf.export()
    assert pfe.keySet().collect{it} == 'dalek_phrase term_code subject'.split(/ /);
    assert pf.export() == [dalek_phrase:[name:        'dalek_phrase',
                                         type:        'SYSTEM',
                                         description: 'dalek_phrase',
                                         label:       'dalek_phrase',
                                         'default':   'Exterminate!'],
                           term_code:[name:           'term_code',
                                      type:           'STRING',
                                      description:    'term_code',
                                      label:          'Term Code',
                                      'default':        '201312',],
                           subject:[name:             'subject',
                                    type:             'STRING',
                                    description:      'subject',
                                    label:            'Subject',
                                    'default':          'ART'],
    ]
    pf.refreshSystemParams()
    // assert pf.matchesSystemParam('dalek_phrase');
    // assert pf.matchesSystemParam('DALEK_PHRASE');
    // assert ! pf.matchesSystemParam('spoon_river');
  }

}

