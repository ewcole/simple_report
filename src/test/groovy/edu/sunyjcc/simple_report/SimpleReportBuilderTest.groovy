package edu.sunyjcc.simple_report

// http://www.javaworld.com/article/2078576/scripting-jvm-languages/groovy-recipes-for-sweeter-ant-builds.html

import groovy.util.BuilderSupport

/** Test the SimpleReportBuilder class. */
public class SimpleReportBuilderTest extends GroovyTestCase {

  void testCreateBuilder() {
    println ""
    println "******** testCreateBuilder ********************"
    def a = new SimpleReportBuilder()
      assert a 
  }

  void testCreateReport() {
    println ""
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
    println ""
    println "******** testReportWithParameters ********************"
    def a = new SimpleReportBuilder()
    def r = a.report(name: "ghostHunt", title: "Ghost Hunt") {
      param(name: 'scoobydoo', default: 'scared')
    }
    println "r=$r"
    assert r
    assert r.name == "ghostHunt"
    assert r.title == "Ghost Hunt"
    assert r.params.params.size() == 1
    def p = r.params.params['scoobydoo']
    assert p?.name == 'scoobydoo'
  }

  /** Try inserting an (unnecessary) params call and see if this works. */
  void testReportWithParamForm() {
    println ""
    println "******** testReportWithParamForm ********************"
    def a = new SimpleReportBuilder()
    def r = a.report(name: "ghostHunt", title: "Ghost Hunt") {
      params {
        param(name: 'scoobydoo', default: 'scared')
      }
    }
  }


  void testCreateParam() {
    println ""
    println "******** testCreateParam ********************"
    def a = new SimpleReportBuilder() 
    def p = a.param(name: 'scoobydoo', default: 'scared',
                    // value: 'hungry'
                   );
    def q = p.export()
    println "p.export() == $q"
    assert q == [name:        "scoobydoo", 
                 type:        "STRING", 
                 description: "scoobydoo", 
                 label:       "Scoobydoo",
                 'default':   "scared",
                 // value:       'hungry'
                ]
  }

  void testCreateParamForm() {
    println ""
    println "******** testCreateParamForm ********************"
    def a = new SimpleReportBuilder()
    def p = a.params {
      param(name: 'scoobydoo', default: 'scared')
      param(name: 'shaggy', label: "Shaggy", default: "hungry")
    }
    assert p
    // Show that we built the right thing.
    assert p.getClass() == ParamForm
    def pe = p.export()
    println pe
    assert pe == [scoobydoo: [name:        "scoobydoo", 
                              type:        "STRING", 
                              description: "scoobydoo", 
                              label:       "Scoobydoo",
                              'default':   "scared",
                              // value:       null
                             ], 
                  shaggy: [name:        "shaggy", 
                           type:        "STRING", 
                           description: "shaggy", 
                           label:       "Shaggy",
                           'default':   "hungry"// ,
                           // value:       null
                          ]]

  }

  void testGetBuildDocs() {
    println ""
    println "******** testGetBuildDocs ********************"
    def a = new SimpleReportBuilder()
    def x = a.getBuildDocs()
    println x
  }

  /** Make sure all of the objects you call in the SimpleReportBuilder 
   *  implement the Buildable interface.
   */
  void testBuildableObjects() {
    println ""
    println "******** testBuildableObjects ********************"
    def a = new SimpleReportBuilder()
    def x = a.getBuildDocs()
    x.each {
      key, val ->
        println "$key: ${val}"
        assert Buildable.getClass().isInstance(val.implClass)
    }
  }

}
