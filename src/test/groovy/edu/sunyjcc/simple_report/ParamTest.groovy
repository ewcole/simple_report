package edu.sunyjcc.simple_report


/** Test the Param class. */
public class ParamTest extends GroovyTestCase {

  void testParamValue() {
    println "******** testParamValue ********************"
    def a = new SimpleReportBuilder()
    def p = a.param(name: 'scoobydoo', default: 'scared')
    def pe = p.export()
    println pe
    assert pe == [name:        "scoobydoo", 
                  type:        "java.lang.String", 
                  description: "scoobydoo", 
                  label:       "scoobydoo", 
                  value:null]
  }

}