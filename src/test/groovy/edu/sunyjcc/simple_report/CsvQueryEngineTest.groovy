package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport

/** Test the SimpleReport class. */
public class CsvQueryEngineTest extends GroovyTestCase {
  void testCreate() {
    def c = new CsvQueryEngine()
    assert c
    println "base constructor OK"
    c = new CsvQueryEngine(file: 'junk.csv')
    assert c
    assert c.file instanceof File
    assert c.file == new File('junk.csv')
    println "file constructor with string value OK"
  }
  void testCreateWithFileObject() {
    def c = new CsvQueryEngine(file: new File('junk.csv'))
    assert c
    assert c.file instanceof File
    assert c.file == new File('junk.csv')
    println "file constructor with file value OK"
    
  }
}