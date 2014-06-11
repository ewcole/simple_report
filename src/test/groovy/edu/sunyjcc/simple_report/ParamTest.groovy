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
                  default:     "scared",
                  label:       "scoobydoo", 
                  value:null]
    pe.value = 'hungry'
    assert pe == [name:        "scoobydoo", 
                  type:        "java.lang.String", 
                  description: "scoobydoo", 
                  default:     "scared",
                  label:       "scoobydoo", 
                  value:       "hungry"]
  }

  void testCreateParamList() {
    println "******** testCreateParamList ********************"
    def params = new SimpleReportBuilder().params {
      param(name: 'term_code', value: '201312')
    }
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'java.lang.String', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   null, 
                  value:       null]
    ]
    println params.export()
  }

  void testSetParamListValues() {
    println "******** testSetParamListValues ********************"
    def params = new SimpleReportBuilder().params {
      param(name: 'term_code', value: '201312')
    }
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'java.lang.String', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   null, 
                  value:       null]
    ]
    println params.export()
    params.setValues(term_code: '201312')
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'java.lang.String', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   null, 
                  value:       '201312']
    ]
  }
}