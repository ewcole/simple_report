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
                  values: [['a',1],['b', 2]]]
  }

}