package edu.sunyjcc.simple_report

import groovy.json.*

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
    def i = rf.getInvocation('paramForm', 'SubjectAndTerm')
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
    def sampleData = [type: "param_form",
                      name: "SubjectAndTerm",
                      isValid: false]
    sampleData.each {
      k, v ->
        assert ie[k] == v
    }
  }

  void testValidate() {
    def rf = getReportObjectFactory();
    assert rf
    def i = rf.getInvocation('paramForm', 'SubjectAndTerm')
    def b = i.validate()
    assert b
  }

  /** See if we get the same param form object each time */
  void testParamFormIdentity() {
    printBanner "testParamFormIdentity"
    def i = getReportObjectFactory().getInvocation('paramForm', 
                                                   'SubjectAndTerm')
    assert i
    def pf0 = i.target

    assert pf0 instanceof ParamForm
    def pf1 = i.getParamFormValue()
    println "pf1=$pf1"
    assert pf1 == i.params
    def pf2 = i.getParamFormValue()
    assert pf1 == pf2
    pf2 = i.params
    assert pf1 == pf2    
  }

  void testSetParamValues() {
    printBanner "testSetParamValues"
    def rf = getReportObjectFactory();
    assert rf
    def i = rf.getInvocation('paramForm', 'SubjectAndTerm')
    println "Before setParamValues()"
    i.setParamValues([term_code: '199712', subject: 'BIO'])
    println "After setParamValues()"
    println "i.params= ${i.params.export()}"
    assert i.params instanceof ParamFormValue
    assert i.params.values.term_code instanceof ParamValue
    println "i.params.values.term_code = ${i.params.values.term_code.export()}"
    assert i.params.values.term_code.value == '199712'
    println "i.params.values.subject = ${i.params.values.subject.export()}"
    assert i.params.values.subject.value == 'BIO'    
  }

  void testRun() {
    printBanner "testRun"
    def rf = getReportObjectFactory();
    assert rf
    def i = rf.getInvocation('paramForm', 'SubjectAndTerm')
    i.setParamValues([term_code: '199712', subject: 'BIO'])
    println "i.params= ${i.params.export()}"
    println "i.params.values.term_code = ${i.params.values.term_code.export()}"
    assert i.params.values.term_code.value == '199712'
    println "i.params.values.subject = ${i.params.values.subject.export()}"
    assert i.params.values.subject.value == 'BIO'
    def s = new StringWriter()
    i.run(OutputFormat.json, s);
    // println "j=$j"
    // def j = new JsonSlurper().parseText(s.toString())
    // assert j.term_code.value == '199712'
    // assert j.subject.value == 'BIO'
  }
}
