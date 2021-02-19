package edu.sunyjcc.simple_report

import groovy.json.*

/** Test the SystemParam class. */
public class SystemParamTest extends GroovyTestCase {
  
  void printBanner(String s) {
    println ""
    println ""
    println ("*" * 79)
    println "******** $s ********************"
    println ("*" * 79)
  }

  /** Get the source directory for the parameter forms, reports, etc. */
  File getSourceDir() {
    //println 'Before Getting Source Dir'
    File f = new File('src/samples/dir1');
    println "Source dir = $f"
    assert f
    assert f.exists()
    return f
  }

  /** Get a FileSourceFactory with the source directory as its root. */
  FileSourceFactory getFileSourceFactory() {
    def d = getSourceDir();
    assert d;
    def fsf = new FileSourceFactory(d)
    assert fsf
    return fsf
  }

  ClientEnv getClientEnv() {
    def ce = new ClientEnv();
    ce.systemParams.a = "a"
    ce.systemParams.b = "{-> 2 + 2}"
    return ce;
  }
  
  ReportObjectFactory getReportObjectFactory() {
    def fsf = getFileSourceFactory();
    def ce = getClientEnv();
    def rof = new ReportObjectFactory(fsf, ce);
    assert rof;
    assert rof.cache
    rof;
  }

  void testHashConstructor() {
    println "********** testHashConstructor **********"
    SystemParam p = new SystemParam(name: 'a',
                                    label: 'parameter a');
    def pe = p.export();
    println "param a = $pe"
    assert pe == [name: 'a',
                  type: 'SYSTEM',
                  description: null,
                  label: 'parameter a',
                  value:  null]
    // Test the value closure
    p = new SystemParam(name:        'b',
                        label:       'param b',
                        description: 'Parameter with value closure',
                        valueClosure: {-> 1 + 1});
    println "param b = ${p.export()}"
    assert p.export() == [name: 'b',
                          type: 'SYSTEM',
                          description: 'Parameter with value closure',
                          label: 'param b',
                          value:  2]
    // Test with value passed in
    p = new SystemParam(name:        'c',
                        label:       'param c',
                        description: 'Parameter with value',
                        value: 42);
    println "param c = ${p.export()}"
    assert p.export() == [name: 'c',
                          type: 'SYSTEM',
                          description: 'Parameter with value',
                          label: 'param c',
                          value:  42]
    
  }

  void testGetParam() {
    println "********** testGetParam **********"
    // Test the value closure
    def p = new SystemParam(name:        'b',
                        label:       'param b',
                        description: 'Parameter with value closure',
                        valueClosure: {-> 1 + 1});
    println "param b = ${p.export()}"
    assert p.export() == [name: 'b',
                          type: 'SYSTEM',
                          description: 'Parameter with value closure',
                          label: 'param b',
                          value:  2]
    Param param = p.getParam();
    println "p.getParam = ${param.export()}"
    assert param.export() == [name: 'b',
                              type: 'SYSTEM',
                              description: 'Parameter with value closure',
                              label: 'param b',
                              default:  2]
  }
 
  void testGetParamValue() {
    println "********** testGetParam **********"
    // Test the value closure
    def p = new SystemParam(name:        'b',
                        label:       'param b',
                        description: 'Parameter with value closure',
                        valueClosure: {-> 1 + 1});
    println "param b = ${p.export()}"
    assert p.export() == [name: 'b',
                          type: 'SYSTEM',
                          description: 'Parameter with value closure',
                          label: 'param b',
                          value:  2]
    ParamValue pv = p.getParamValue();
    println "pv.getParamValue() = ${pv.export()}"
    assert pv.export() == [name: 'b',
                           type: 'SYSTEM',
                           description: 'Parameter with value closure',
                           label: 'param b',
                           'default': 2,
                           value:  2]
  }

  void testToJson() {
    println "********** testToJson **********"
    def p = new SystemParam(name:        'b',
                            label:       'param b',
                            description: 'Parameter with value closure',
                            valueClosure: {-> 1 + 1});
    println "param b = ${p.export()}"
    String json1 = p.toJson();
    println "json1 = $json1"
    def pj = new JsonSlurper().parseText(json1);
    assert pj == [name: 'b',
                  type: 'SYSTEM',
                  description: 'Parameter with value closure',
                  label: 'param b',
                  value:  2]
    // ParamValue pv = p.getParamValue();
    // println "pv.getParamValue() = ${pv.export()}"
    // assert pv.export() == [name: 'b',
    //                        type: 'SYSTEM',
    //                        description: 'Parameter with value closure',
    //                        label: 'param b',
    //                        'default': 2,
    //                        value:  2]    
  }
}
