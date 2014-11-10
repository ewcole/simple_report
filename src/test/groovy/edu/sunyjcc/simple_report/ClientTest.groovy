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
    printBanner "testCreateClient";
    def c = new PogoClient()
  }

  void testCreateClientWithFactory() {
    printBanner "testCreateClientWithFactory";
    def factory = getReportObjectFactory();
    def c = new PogoClient(factory)
  }

  void testShowParam() {
    printBanner "testShowParam";
    def factory = getReportObjectFactory();
    def c = new PogoClient(factory)
    def i = factory.getInvocation('param_form', 'SubjectAndTerm')
    assert c.showParam(i, 'subject') == [name:        'subject', 
                                         type:        'STRING', 
                                         description: 'subject', 
                                         label:       'subject', 
                                         'default':   'ART', 
                                         value:       'ART']
    assert c.showParam(i, 'term_code') == [name:        'term_code', 
                                           type:        'STRING', 
                                           description: 'term_code', 
                                           label:       'term_code', 
                                           'default':   '201312', 
                                           value:       '201312']
  }
  void testShowParamForm() {
    printBanner "testShowParamForm";
    def factory = getReportObjectFactory();
    def c = new PogoClient(factory)
    def i = factory.getInvocation('param_form', 'SubjectAndTerm')
    assert c.showParamForm(i) == [params: [name:        'subject', 
                                           type:        'STRING', 
                                           description: 'subject', 
                                           label:       'subject', 
                                           'default':   'ART', 
                                           value:       'ART'],
                                  [name:        'term_code', 
                                   type:        'STRING', 
                                   description: 'term_code', 
                                   label:       'term_code', 
                                   'default':   '201312', 
                                   value:       '201312']]
  }

  
}