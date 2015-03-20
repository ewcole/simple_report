package edu.sunyjcc.simple_report

/** Test the SimpleReportBuilder class. */
public class ColumnListTest extends GroovyTestCase {
  
  String banner(String title) {
    println (("*" * 10) + " $title " + ('*' * 10))
  }

  void testLeftShift() {
    banner "testLeftShift"
    def cl = new ColumnList()
    def columnNames = "a b c d e".split(' ')
    columnNames.each {
      cl << new Column(name: it, label: "${it} - first pass")
    }
    println "$cl"
    assert cl.list().collect{it.name} == columnNames
    def columnNames2 = columnNames + columnNames.reverse()
    // Show that attempting to re-add columns has no effect.
    columnNames.each {
      cl << new Column(name: it, label: "${it} - second pass")
    }
    assert cl.list().collect{it.name} == columnNames
    cl.list().each {
      println "it.label == ${it.label}"
      assert it.label == "${it.name} - first pass"
    }
  }

  void testExport() {
    banner "testExport"
    def cl = new ColumnList()
    def columnNames = "a b c d e".split(' ')
    columnNames.each {
      cl << new Column(name: it, label: "${it} - first pass")
    }
    println "$cl"
    println "-- Columns --"
    cl.each {
      println it.export()
    }
    println "-- --"
    assert cl.export() == columnNames.collect {
      cl.columns[it].export()
    }
  }

}