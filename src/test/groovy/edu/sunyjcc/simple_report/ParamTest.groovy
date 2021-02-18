package edu.sunyjcc.simple_report


/** Test the Param class. */
public class ParamTest extends GroovyTestCase {

  void testParamCreate() {
    println "******** testParamCreate ********************"
    def a = new SimpleReportBuilder()
    def p = a.param(name: 'scoobydoo', default: 'scared')
    def pe = p.export()
    println pe
    assert pe == [name:        "scoobydoo", 
                  type:        "STRING", 
                  description: "scoobydoo", 
                  default:     "scared",
                  label:       "scoobydoo"]
    assert p.export() == [name:        "scoobydoo", 
                          type:        "STRING", 
                          description: "scoobydoo", 
                          'default':     "scared",
                          label:       "scoobydoo"]
  }

  void testCreateParamForm() {
    println "******** testCreateParamForm ********************"
    def params = new SimpleReportBuilder().params {
      param(name: 'term_code', default: '201312')
    }
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'STRING', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   '201312']
    ]
    println params.export()
  }

  void testSetParamFormValues() {
    println "******** testSetParamFormValues ********************"
    def params = new SimpleReportBuilder().params {
      param(name: 'term_code', default: '201312')
    }
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'STRING', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   '201312']
    ]
    println params.export()
    //params.setParamValues(term_code: '201312')
    println (params.export())
    //assert params.term_code.currentValue == '201312'
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'STRING', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   '201312']
    ]
  }

  void testGetParamFormValues() {
    println "******** testGetParamFormValues ********************"
    def params = new SimpleReportBuilder().params {
      param(name: 'term_code', default: '201312')
      param(name: 'subject',   default: 'ART')
    }
    assert params.export() == [
      subject: [name:        'subject', 
                type:        'STRING', 
                description: 'subject', 
                label:       'subject', 
                "default":   'ART'],
      term_code: [name:        'term_code', 
                  type:        'STRING', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   '201312']
    ]
    println params.export()
    //def gv = params.getValues()
    //println "gv=${params.getValues()}"
    //assert gv == [term_code: '201312', subject: 'ART']
  }
}