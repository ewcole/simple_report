package edu.sunyjcc.simple_report

/** Test the ListOfValues class. */
public class ListOfValuesTest extends GroovyTestCase {

  void testListOfValuesCreate() {
    println "******** testListOfValuesCreate ********************"
    def a = new SimpleReportBuilder()
    def l = a.list_of_values(values: [['a',1],['b', 2]])
    assert l
    def le = l.export()
    println le
    assert le == [type: 'value_list',
                  values: [[value: 'a', desc: 1],
                           [value: 'b', desc: 2]]]
  }

 void testListOfValuesExecute() {
    println "******** testListOfValuesExecute ********************"
    def a = new SimpleReportBuilder()
    def l = a.list_of_values(values: [['a',1],['b', 2]])
    assert l
    def le = l.values
    println le
    assert le == [[value: 'a', desc: 1],
                  [value: 'b', desc: 2]]
  }

 void testSqlListOfValuesCreate() {
    println "******** testSqlListOfValuesCreate ********************"
    def a = new SimpleReportBuilder()
    def l = a.list_of_values(query: "select user from dual")
    assert l
    def le = l.export()
    println le
    assert le == [type:   "sql",
                  query:  "select user from dual"]
  }

}