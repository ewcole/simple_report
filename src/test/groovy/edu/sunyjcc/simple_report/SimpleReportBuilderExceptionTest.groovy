package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport

/** Test the SimpleReportBuilder class's exception handling. */
public class SimpleReportBuilderExceptionTest extends GroovyTestCase {

  void testInvalidMethodName() {
    println "******** testInvalidMethodName ********"
    def a = new SimpleReportBuilder()
    String text = 'report() { parm name: "cheese"}'
    println "report text = '$text'"
    try {
      def r = a.eval(text);
      // eval should throw an exception.
      assert ! r
    } catch (BuildException e) {
      println "Message: ${e.message}"
      println "${e.source}"
      assert e.source == text
    }
  }

  void testInvalidNesting() {
    println "******** testInvalidNesting ********"
    def a = new SimpleReportBuilder()
    String text = '''report() { param name: "cheese"; report(name:'frenzy') {}}'''
    println "report text = '$text'"
    try {
      def r = a.eval(text);
      // eval should throw an exception.
      assert ! r
    } catch (BuildException e) {
      println "Message: ${e.message}"
      println "${e.source}"
      assert e.source == text
    }
  }

}