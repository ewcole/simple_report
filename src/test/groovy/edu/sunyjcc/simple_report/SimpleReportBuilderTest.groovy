package edu.sunyjcc.simple_report

// http://www.javaworld.com/article/2078576/scripting-jvm-languages/groovy-recipes-for-sweeter-ant-builds.html

import groovy.util.BuilderSupport

/** Test the SimpleReportBuilder class. */
public class SimpleReportBuilderTest extends GroovyTestCase {

  void testCreateBuilder() {
    def a = new SimpleReportBuilder()
      assert a 
  }

  void testCreateReport() {
    def a = new SimpleReportBuilder()
    def r = a.report(name: "ghostHunt") {
      param(name: 'scoobydoo', default: 'scared')
    }
    println "r=$r"
    assert r
    assert r.report.name == "ghostHunt"
  }
}