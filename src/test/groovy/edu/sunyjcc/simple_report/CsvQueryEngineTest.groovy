package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport

/** Test the SimpleReport class. */
public class CsvQueryEngineTest extends GroovyTestCase {

  def printBanner(String text) {
    println "********** $text ********************"
  }

  SimpleReportBuilder getBuilder() {
    new SimpleReportBuilder()
  }

  void testCreate() {
    printBanner "testCreate"
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
    printBanner "testCreateWithFileObject"
    def c = new CsvQueryEngine(file: new File('junk.csv'))
    assert c
    assert c.file instanceof File
    assert c.file == new File('junk.csv')
    println "file constructor with file value OK"
  }

  /** If you pass both file and text to the engine, it should ignore the text.*/
  void testCreateWithFileAndText() {
    printBanner "testCreateWithFileAndText"
    def c = new CsvQueryEngine(file: new File('junk.csv'), text: 'text')
    assert c
    assert c.file instanceof File
    assert c.file == new File('junk.csv')
    assert c.text == null
    println "file constructor with file and text OK"
  }

  /** Build a new instance with a text parameter.*/
  void testCreateWithText() {
    printBanner "testCreateWithText"
    def c = new CsvQueryEngine(text: 'text')
    assert c
    assert c.file == null
    assert c.text == 'text'
    println "text constructor OK"
  }

  void testExportWhenNoTextOrFile() {
    printBanner "testExportWhenNoTextOrFile"
    def c = new CsvQueryEngine()
    assert c
    println c
    def e = c.export()
    println "c.export() == $e"
    assert e
  }

  void testExecute() {
    printBanner "testExecute"
    def csve = getBuilder().csv(text: "a,b,c\r\n1,2,3\n4,5,6")
    assert csve instanceof CsvQueryEngine
    def r = csve.execute()
    assert r instanceof ResultSet
    println r.export()
    assert r.rows.size() == 2
    assert r.columns.size() == 3;
    assert r.columns.collect{it.name} == "a b c".split(' ')
    println "columns=$r.columns"
    r.rows.eachWithIndex {rw, i -> println "row[$i] = ${rw}"}
    assert r.rows[0] == [a: "1", b: "2", c: "3"]
    assert r.rows[1] == [a: "4", b: "5", c: "6"]
  }
}