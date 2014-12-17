package edu.sunyjcc.simple_report

// http://www.javaworld.com/article/2078576/scripting-jvm-languages/groovy-recipes-for-sweeter-ant-builds.html

/** Test the SimpleReportBuilder class. */
public class SimpleReportBuilderCsvTest extends GroovyTestCase {
  void testCreateCsvQueryEngine() {
    println "******** testCreateCsvQueryEngine ********************"
    def a = new SimpleReportBuilder()
    def e = a.csv()
    println "e = $e"
    assert e
    def exp = e.export()
    println "new csv query engine=${exp}"
    assert exp == [queryEngineType: 'csv', 
                   class: 'edu.sunyjcc.simple_report.CsvQueryEngine']
    println ("*" * 70)
    println()
  }

  void testCreateCsvQueryEngineWithText() {
    println "******** testCreateCsvQueryEngineWithText ********************"
    def a = new SimpleReportBuilder()
    def e = a.csv(text: "A, B, C\n1,2,3")
    assert e
    def exp = e.export()
    println "new csv query engine=${exp}"
    println ("*" * 70)
    println()
    assert exp == [queryEngineType: 'csv', 
                   class: 'edu.sunyjcc.simple_report.CsvQueryEngine',
                   text: "A, B, C\n1,2,3"]
    println ("*" * 70)
    println()
  }
  
  void testReportWithCsvQueryEngine() {
    println "******** testReportWithCsvQueryEngine ********************"
    def a = new SimpleReportBuilder()
    def e = a.report() {
      csv(text: "A, B, C\n1,2,3"){}
    }
    println "e=$e"
    assert e
    def exp = e.export()
    println "new csv query engine=${exp}"
    //def i = new Invocation(e)
    //println "i = $i"
    //assert ex == [A: 1, B: 2, C: 3]

    println ("*" * 70)
    println()
  }
}