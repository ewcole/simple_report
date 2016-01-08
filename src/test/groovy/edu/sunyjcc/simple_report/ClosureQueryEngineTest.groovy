package edu.sunyjcc.simple_report

public class ClosureQueryEngineTest extends GroovyTestCase {

  def banner(String text) {
    println "******** $text ********************"
  }

  void testCreateClosureQueryEngine() {
    println "******** testCreateClosureQueryEngine ********************"
    ClosureQueryEngine e = new ClosureQueryEngine()
    println "e=$e"
    println "e.getClass() = ${e.getClass()}"
    assert e instanceof ClosureQueryEngine
    assert e instanceof QueryEngine
    assert e.queryEngineType == "closure"
    println "e = $e"
    assert e != null
    assert e
    assert e instanceof ClosureQueryEngine
    // e.getClass().methods.each {
    //   println "method: $it"
    // }
    def exp = e.export()
    println "new closure query engine=${exp}"
    assert exp == [queryEngineType: 'closure', 
                   class: 'edu.sunyjcc.simple_report.ClosureQueryEngine']
    println ("*" * 70)
    println()
  }

  void testBuildClosureQueryEngine() {
    println "******** testBuildClosureQueryEngine ********************"
    def a = new SimpleReportBuilder()
    ClosureQueryEngine e = a.data_script()
    assert e instanceof ClosureQueryEngine
    assert e.queryEngineType == "closure"
    println "e = $e"
    assert e != null
    assert e
    assert e instanceof ClosureQueryEngine
    // e.getClass().methods.each {
    //   println "method: $it"
    // }
    def exp = e.export()
    println "new closure query engine=${exp}"
    assert exp == [queryEngineType: 'closure', 
                   class: 'edu.sunyjcc.simple_report.ClosureQueryEngine']
    println ("*" * 70)
    println()
  }

  void testReportWithClosureQueryEngine() {
    println "******** testReportWithClosureQueryEngine ********************"
    def a = new SimpleReportBuilder()
    def e = a.report() {
      data_script closure: {
      }
    }
    println "e=$e"
    assert e
    def exp = e.export()
    println "new closure query engine=${exp}"
    println ("*" * 70)
  }

  void testExecute() {
    println "******** testExecute ********************"
    def a = new SimpleReportBuilder()
    def e = a.report() {
      data_script (script: {
                     // column(name: "num", label: "Number")
                     // column(name: "square", label: "Square")
                     // column(name: "cube")
                     (1..3).each {
                       row(num: it, square: it*it, cube: it*it*it)
                     }
                   })
    }
    assert e instanceof SimpleReport
    println "Engine=${e.queryEngine}";
    def engine = e.queryEngine
    assert engine instanceof ClosureQueryEngine;
    assert engine.dataScript
    assert engine.dataScript instanceof Closure
    def r = e.execute([:])
    println "r.columns = ${r.columns.export()}"
    println "r.rows = ${r.rows}"
    assert r.rows.size() == 3;
    assert r.rows[0] == [num: 1, square: 1, cube: 1];
    assert r.columns.columnNames.size() == 3
  }

  void testGetQueryClosureDocs() {
    banner "testGetQueryClosureDocs"
    println ClosureQueryEngine.queryClosureDocs
  }
}
