package edu.sunyjcc.simple_report

/** Test the SimpleReportBuilder class. */
public class SimpleReportBuilderSqlTest extends GroovyTestCase {

  void testCreateSqlQueryEngine() {
    println "******** testCreateSqlQueryEngine ********************"
    def a = new SimpleReportBuilder()
    def e = a.sql(query: 'select user from dual')
    println "e = $e"
    assert e
    def exp = e.export()
    println "new sql query engine=${exp}"
    assert exp == [queryEngineType: 'sql', 
                   class: 'edu.sunyjcc.simple_report.SqlQueryEngine',
                   query:       'select user from dual',
                   parsedQuery: 'select user from dual',
                   paramRefs:   [],]
    println ("*" * 70)
    println()
  }

  void testCreateSqlQueryEngineWithText() {
    println "******** testCreateSqlQueryEngineWithText ********************"
    def a = new SimpleReportBuilder()
    def e = a.sql(query: "select user from dual")
    assert e
    def exp = e.export()
    println "new sql query engine=${exp}"
    println ("*" * 70)
    println()
    assert exp == [queryEngineType: 'sql', 
                   class: 'edu.sunyjcc.simple_report.SqlQueryEngine',
                   query: "select user from dual",
                   parsedQuery: "select user from dual",
                   paramRefs:   [],]
    println ("*" * 70)
    println()
  }
  
  void testReportWithSqlQueryEngine() {
    println "******** testReportWithSqlQueryEngine ********************"
    def a = new SimpleReportBuilder()
    def e = a.report() {
      sql(query: "select user from dual"){}
    }
    println "e=$e"
    assert e
    def exp = e.export()
    println "new sql query engine=${exp}"
    println ("*" * 70)
    println()
    assert exp == [name:    "", 
                   title:   "", 
                   version: "", 
                   description:null, 
                   params:null, 
                   columns:null,
                   queryEngine: [queryEngineType: 'sql', 
                                 class: 'edu.sunyjcc.simple_report.SqlQueryEngine',
                                 query: "select user from dual",
                                 parsedQuery: "select user from dual", 
                                 paramRefs:[],]]
  }

  
}