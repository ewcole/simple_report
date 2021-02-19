package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport

/** Test the SimpleReportInstance class. */
public class SimpleReportInstanceTest extends GroovyTestCase {

  /** Get the source directory for the parameter forms, reports, etc. */
  File getSourceDir() {
    File f = new File('src/samples/dir1');
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

  SimpleReport getReport(String name) {
    getReportObjectFactory().getReport(name)
  }

  def printBanner(String text) {
    println "********** $text ********************"
  }

  def report1Name = 'simple_report_types'

  void testCreateReport() {
    printBanner "testCreateReport"
    def r = getReportObjectFactory().getReport(report1Name)
    assert r
    println "${r.export()}"
  }

  void testRunAsHTML() {
    printBanner "testRunAsHTML"
    def r = getReport(report1Name)
    assert r
    println "Report ${r.name}" 
    def pf = r.getParamFormValue()
    def s = new StringWriter()
    assert r.run(OutputFormat.html, pf, s)
    println "$s"
    assert s.toString().size()
  }

  // Turn off text output for now.
  // void testRunAsText() {
  //   printBanner "testRunAsTEXT"
  //   def r = getReport(report1Name)
  //   assert r
  //   println "Report ${r.name}" 
  //   def pf = r.getParamFormValue()
  //   def s = new StringWriter()
  //   assert r.run(OutputFormat.text, pf, s)
  //   println "$s"
  //   assert s.toString().size()
  //   def l2 = ["",
  //             "Name Description",
  //             "---- -----------",
  //             "param A single parameter",
  //             "param_form A parameter form",
  //             "report A groovy-based report",
  //             "jrxml A Jasper Report",
  //             "4 rows selected."]
  //   def lines = s.toString().readLines()
  //   assert lines == l2;
  // }

}
