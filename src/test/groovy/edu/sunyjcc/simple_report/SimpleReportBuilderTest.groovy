package edu.sunyjcc.simple_report

// http://www.javaworld.com/article/2078576/scripting-jvm-languages/groovy-recipes-for-sweeter-ant-builds.html

import groovy.util.BuilderSupport

/** Test the SimpleReportBuilder class. */
public class SimpleReportBuilderTest extends GroovyTestCase {

  void testCreateBuilder() {
    println "******** testCreateBuilder ********************"
    def a = new SimpleReportBuilder()
      assert a 
  }

  void testCreateReport() {
    println "******** testCreateReport ********************"
    def a = new SimpleReportBuilder()
    def r = a.report(name: "ghostHunt", title: "Ghost Hunt") //{
      //     param(name: 'scoobydoo', default: 'scared')
    //}
    println "r=$r"
    assert r
    assert r.name == "ghostHunt"
    assert r.title == "Ghost Hunt"
  }

  void testReportWithParameters() {
    println "******** testReportWithParameters ********************"
    def a = new SimpleReportBuilder()
    def r = a.report(name: "ghostHunt", title: "Ghost Hunt") {
      param(name: 'scoobydoo', default: 'scared')
    }
    println "r=$r"
    assert r
    assert r.name == "ghostHunt"
    assert r.title == "Ghost Hunt"
    assert r.params.size() == 1
    def p = r.params['scoobydoo']
    assert p?.name == 'scoobydoo'
  }

  /** Try inserting an (unnecessary) params call and see if this works. */
  void testReportWithParamList() {
    println "******** testReportWithParamList ********************"
    def a = new SimpleReportBuilder()
    def r = a.report(name: "ghostHunt", title: "Ghost Hunt") {
      params {
        param(name: 'scoobydoo', default: 'scared')
      }
    }
  }


  void testCreateParam() {
    println "******** testCreateParam ********************"
    def a = new SimpleReportBuilder() 
    def p = a.param(name: 'scoobydoo', default: 'scared',
                    value: 'hungry');
    def q = p.export()
    println "p.export() == $q"
    assert q == [name:        "scoobydoo", 
                 type:        "STRING", 
                 description: "scoobydoo", 
                 label:       "scoobydoo",
                 'default':   "scared",
                 value:       'hungry']
  }

  void testCreateParamList() {
    println "******** testCreateParamList ********************"
    def a = new SimpleReportBuilder()
    def p = a.params {
      param(name: 'scoobydoo', default: 'scared')
      param(name: 'shaggy', label: "Shaggy", default: "hungry")
    }
    assert p
    // Show that we built the right thing.
    assert p.getClass() == ParamList
    def pe = p.export()
    println pe
    assert pe == [scoobydoo: [name:        "scoobydoo", 
                              type:        "STRING", 
                              description: "scoobydoo", 
                              label:       "scoobydoo",
                              'default':   "scared",
                              value:       null], 
                  shaggy: [name:        "shaggy", 
                           type:        "STRING", 
                           description: "shaggy", 
                           label:       "Shaggy",
                           'default':   "hungry",
                           value:       null]]

  }

}