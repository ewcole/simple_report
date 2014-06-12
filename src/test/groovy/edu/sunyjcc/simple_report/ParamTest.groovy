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
    p.value = 'hungry'
    println "p.currentValue == ${p.currentValue}"
    assert p.currentValue == 'hungry'
    assert p.export() == [name:        "scoobydoo", 
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
                  value:       '201312']
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
                  value:       '201312']
    ]
    println params.export()
    params.setValues(term_code: '201312')
    println (params.export())
    assert params.term_code.currentValue == '201312'
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'java.lang.String', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   null, 
                  value:       '201312']
    ]
  }

  void testGetParamListValues() {
    println "******** testGetParamListValues ********************"
    def params = new SimpleReportBuilder().params {
      param(name: 'term_code', value: '201312')
    }
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'java.lang.String', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   null, 
                  value:       '201312']
    ]
    println params.export()
    def gv = params.getValues()
    println "gv=${params.getValues()}"
    assert gv == [term_code: '201312']
  }
}