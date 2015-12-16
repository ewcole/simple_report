package edu.sunyjcc.simple_report


/** Test the Param class. */
public class ParamValueTest extends GroovyTestCase {

  Param getParam() {
    def a = new SimpleReportBuilder()
    def p = a.param(name: 'scoobydoo', default: 'scared')
  }

  void testParamValueConstructor1() {
    println "******** testParamValueConstructor1 ********************"
    def p = getParam();
    def pe = p.export()
    println pe
    assert pe == [name:        "scoobydoo", 
                  type:        "STRING", 
                  description: "scoobydoo", 
                  default:     "scared",
                  label:       "scoobydoo"]
    def pv = new ParamValue(p, 'hungry');
    println "pv.currentValue == ${pv.currentValue}"
    assert pv.currentValue == 'hungry'
    assert pv.export() == [name:        "scoobydoo", 
                           type:        "STRING", 
                           description: "scoobydoo", 
                           'default':   "scared",
                           label:       "scoobydoo",
                           value:       "hungry"]
  }

  void testParamValueConstructor2() {
    println "******** testParamValueConstructor2 ********************"
    def p = getParam();
    def pe = p.export()
    println pe
    assert pe == [name:        "scoobydoo", 
                  type:        "STRING", 
                  description: "scoobydoo", 
                  default:     "scared",
                  label:       "scoobydoo"]
    def pv = new ParamValue(p);
    pv.currentValue = 'hungry'
    println "pv.currentValue == ${pv.currentValue}"
    assert pv.currentValue == 'hungry'
    assert pv.export() == [name:        "scoobydoo", 
                           type:        "STRING", 
                           description: "scoobydoo", 
                           'default':   "scared",
                           label:       "scoobydoo",
                           value:       "hungry"]
    pv.value = 'spooked';
    assert pv.export() == [name:        "scoobydoo", 
                           type:        "STRING", 
                           description: "scoobydoo", 
                           'default':   "scared",
                           label:       "scoobydoo",
                           value:       "spooked"]
  }

  void testNumberParamValue() {
    println "${'*' * 10} testNumberParamValue ${'*' * 10}"
    def a = new SimpleReportBuilder()
    def p = a.param(name: 'scoobydoo', 
                    type: 'number',
                    'default': 12);
    def pv = new ParamValue(p);
    pv.currentValue = 42;
    assert pv.currentValue == 42;
    def pve = pv.export()
    assert pve.name        == "scoobydoo";
    assert pve.type        == "NUMBER";
    assert pve.description == "scoobydoo";
    assert pve.label       == "scoobydoo";
    assert pve."default"   == 12;
    println "value class = ${pv.currentValue.getClass()}"
    assert pve.value       == 42;
    assert pv.export() == [name:        "scoobydoo", 
                           type:        "NUMBER", 
                           description: "scoobydoo", 
                           label:       "scoobydoo",
                           'default':   12,
                           value:       42]
    // Try assigning a string to the value.
    pv.currentValue = "43"
    assert pv.currentValue == 43
  }
}