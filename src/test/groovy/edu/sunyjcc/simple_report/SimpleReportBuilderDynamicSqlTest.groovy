package edu.sunyjcc.simple_report

/** Test the SimpleReportBuilder class. */
public class SimpleReportBuilderDynamicSqlTest extends GroovyTestCase {

  void testCreateDynamicSqlQueryEngine() {
    println "******** testCreateDynamicSqlQueryEngine ********************"
    def a = new SimpleReportBuilder()
    def e = a.dynamic_sql(sql_closure: {params -> "select 'xx' from dual"})
    println "e = $e"
    assert e
    def exp = e.export()
    println "new sql query engine=${exp}"
    assert exp == [queryEngineType: 'dynamic_sql', 
                   class: 'edu.sunyjcc.simple_report.DynamicSqlQueryEngine']
    println ("*" * 70)
    println()
  }

  void testCreateDynamicSqlQueryEngineWithText() {
    println "******** testCreateDynamicSqlQueryEngineWithText ********************"
    def a = new SimpleReportBuilder()
    def e = a.dynamic_sql(sql_closure: {params -> "select 'xx' from dual"})
    assert e
    def exp = e.export()
    println "new sql query engine=${exp}"
    println ("*" * 70)
    println()
    assert exp == [queryEngineType: 'dynamic_sql', 
                   class: 'edu.sunyjcc.simple_report.DynamicSqlQueryEngine']
    println ("*" * 70)
    println()
  }
  
  void testReportWithDynamicSqlQueryEngine() {
    println "******** testReportWithDynamicSqlQueryEngine ********************"
    def a = new SimpleReportBuilder()
    def e = a.report() {
      dynamic_sql(sql_closure: {params -> "select 'xx' from dual"}){}
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
                   queryEngine: [queryEngineType: 'dynamic_sql', 
                                 class: 'edu.sunyjcc.simple_report.DynamicSqlQueryEngine']]
  }

  void testGetSqlText() {
    println "******** testGetSqlText ********************"
    def a = new SimpleReportBuilder()
    def e = a.dynamic_sql(sql_closure: {
                            params -> 
                              "select ${params.col} from ${params.table}"
                          })
    println "e = $e"
    assert e
    def sqt = e.getSqlText(col: 'foo', table: 'bar')
    println "sqt = $sqt"
    assert sqt == "select foo from bar"
    println ("*" * 70)
    println()
  }

 
 
}