package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport

/** Test the CSV output created by the report. */
public class SimpleReportCsvOutputTest extends GroovyTestCase {

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

  
  def getReportFromListOfHashes(def data) {
    def r = builder.report {
      data_generator closure: {
        // data[0].keySet().each { column(name: it)};
        data.each {
          row (it)
        }
      }
    }
    def pf = r.getParamFormValue()
    def s = new StringWriter();
    assert r.run(OutputFormat.csv, pf, s);
    def output = s.toString()
  }
  
  def printBanner(String text) {
    println "********** $text ********************"
  }

  def getBuilder() {
    new SimpleReportBuilder(getReportObjectFactory());
  }
  
  void testPlainCsvOutput() {
    printBanner "testPlainCsvOutput"
    def r = getReport('simple_report_types')
    assert r
    println "Report ${r.name}" 
    def pf = r.getParamFormValue()
    def s = new StringWriter()
    assert r.run(OutputFormat.csv, pf, s)
    println "$s"
    assert s.toString().size()
    def sampleSrc = '''"Name","Description"
                       "param","A single parameter"
                       "param_form","A parameter form"
                       "report","A groovy-based report"
                       "jrxml","A Jasper Report"'''.readLines().collect{
      it.replaceAll(/^\s+/,'')
    }
    assert s.toString().readLines() == sampleSrc
  }

  void testReportSampler() {
    def output = getReportFromListOfHashes([
        [a: 1, b: 2, c: 3],
        [b: 2, c: 3, d: 4],
      ]).readLines();
    assert output.size();
    assert output == ['"a","b","c","d"',
                      '"1","2","3",""',
                      '"","2","3","4"']
  }

  void testEmbeddedQuote() {
    def output = getReportFromListOfHashes([
        [a: "1", b: 'ab\"c', c: "3"]
      ]).readLines();
    assert output.size();
    assert output == ['"a","b","c"', '"1","ab""c","3"']
  }

  void testEmbeddedComma() {
    def output = getReportFromListOfHashes([
        [a: "1", b: 'ab,c', c: "3"]
      ]).readLines();
    assert output.size();
    assert output == ['"a","b","c"', '"1","ab,c","3"']
  }
  void testEmbeddedNewLine() {
    def output = getReportFromListOfHashes([
        [a: "1", b: '''abc
         d''', c: "3"]
      ]).readLines();
    assert output.size();
    assert output == ['"a","b","c"', '"1","abc', '         d","3"']
  }
}
