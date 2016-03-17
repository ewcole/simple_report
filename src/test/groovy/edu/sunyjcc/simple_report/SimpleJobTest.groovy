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
    j.addParam(new Param(name: 'a', title: 'parameter a'));
    j.addParam(new Param(name: 'b', title: 'parameter b'));
    j.addParam(new Param(name: 'c', title: 'parameter c'));
    assert j.export() == [name: 'testJob',
                          type: 'SimpleJob',
                          title: 'Job for testing',
                          version: '',
                          description: null,
                          params: null];
    def jobEngine = {
      ->
      markupBuilder.output {
        ('a'..'c').each {
          "$it"(value: "${params[it]}", "${params[it]}")
        }
      }
    }
    String s = new MarkupBuilder().output {
        ('a'..'c').each {
          "$it"(value: "*${it}*", "*${it}*")
        }      
    }
    j.jobEngine = jobEngine;
    def sb = new StringBuffer();
    def m = new MarkupBuilder(sb);
    j.execute(('a'..'c').inject([:]) {
                map, v ->
                  map << ["$v": "*$v*"]
              }, m)
    assert sb.toString() == s
  }
}
