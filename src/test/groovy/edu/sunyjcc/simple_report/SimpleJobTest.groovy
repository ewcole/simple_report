package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport
import groovy.xml.*

/** Test the SimpleJob class. */
public class SimpleJobTest extends GroovyTestCase {
  static void banner(String text) {
    println "${'*'*15} $text ${'*'*15}"
  }

  void testCreateSimpleJob() {
    banner ('testCreateSimpleJob')
    SimpleJob j = new SimpleJob();
    assert j;
    j = new SimpleJob(name: 'testJob',
                      title: 'Job for testing');
    assert j.export() == [name: 'testJob',
                          type: 'SimpleJob',
                          title: 'Job for testing',
                          version: '',
                          description: null,
                          params: null];
  }

  void testExecuteSimpleJob() {
    banner ('testExecuteSimpleJob')
    println " - Test execute function for simple job -"
    SimpleJob j = new SimpleJob(name: 'testExecute',
                      title: 'Test to see if we can execute a job');
    j.addParam(new Param(name: 'a', label: 'parameter a'));
    j.addParam(new Param(name: 'b', label: 'parameter b'));
    j.addParam(new Param(name: 'c', label: 'parameter c'));
    assert j.export() == [name: 'testExecute',
                          type: 'SimpleJob',
                          title: 'Test to see if we can execute a job',
                          version: '',
                          description: null,
                          params: [a: [name: 'a', 
                                       type: 'STRING', 
                                       description: null, 
                                       label: 'parameter a', 
                                       "default":null], 
                                   b: [name: 'b', 
                                       type: 'STRING', 
                                       description: null, 
                                       label: 'parameter b', 
                                       "default": null], 
                                   c: [name: 'c', 
                                       type: 'STRING', 
                                       description: null, 
                                       label: 'parameter c', 
                                       "default": null]]];
    def paramVals = ('a'..'c').inject([:]) {
      map, v ->
        map << ["$v": "*$v*"]
    }
    println "paramVals=$paramVals"
    def writeOutput = {
      mb, hsh ->
        mb.output {
          ('a'..'c').each {
            "$it"(value: "${hsh[it]}", "${hsh[it]}")
          }
        }
    }
    def jobEngine = {
      ->
      writeOutput(markupBuilder, params);
    }
    def s = new StringWriter()
    writeOutput(new MarkupBuilder(s), paramVals);
    j.jobEngine = jobEngine;
    def sw = new StringWriter();
    def m = new MarkupBuilder(sw);
    j.execute(('a'..'c').inject([:]) {
                map, v ->
                  map << ["$v": "*$v*"]
              }, m)
    assert sw.toString() == s.toString()
  }
}
