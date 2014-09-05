package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport

/** Test the SimpleReport class. */
public class SimpleReportTest extends GroovyTestCase {

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
    def r2 = r.export()
    println "r2=$r2"
    assert r2== [name: "ghostHunt", 
                 title: "Ghost Hunt", 
                 version: "", 
                 description: null, 
                 params:null,
                 columns: null,
                 queryEngine: null]
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
    assert p.name == 'scoobydoo'
    println "${r.export()}"
  }

  /** Try inserting an (unnecessary) params call and see if this works. */
  void testReportWithParamList() {
    println "******** testReportWithParamList ********************"
    def a = new SimpleReportBuilder()
    def r = a.report(name: "ghostHunt", title: "Ghost Hunt") {
      params {
        param(name: 'scoobydoo', label: "Scooby Doo", default: 'scared')
      }
    }
    println r.export()
    assert r.export() == [name: "ghostHunt", 
                          title: "Ghost Hunt", 
                          version: "", 
                          description: null,
                          params:[scoobydoo: [name:        "scoobydoo", 
                                              type:        "STRING", 
                                              description: "scoobydoo", 
                                              label:       "Scooby Doo",
                                              'default':    "scared",
                                              value:       null]],
                          columns: null,
                          queryEngine: null,]
  }
}