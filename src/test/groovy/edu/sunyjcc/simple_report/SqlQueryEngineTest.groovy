package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport

/** Test the SqlQueryEngine class. */
public class SqlQueryEngineTest extends GroovyTestCase {

  void testParseSqlNoParams () {
    println "**** testParseSqlNoParams ****************"
    def query = "select user from dual"
    def pq = SqlQueryEngine.parseSql(query)
    println "$pq"
    println ("*"*70)
    println ()
    assert pq == [paramRefs:[], parsedQuery: "select user from dual"]
  }

  void testParseSqlWithParams () {
    println "**** testParseSqlNoParams ****************"
    def query = "select puppies from daisy_hill where name = :snoopy " +
                "and breed=:beagle"
    def pq = SqlQueryEngine.parseSql(query)
    println "$pq"
    println ("*"*70)
    println ()
    assert pq == [paramRefs:['snoopy', 'beagle'], 
                  parsedQuery: "select puppies from daisy_hill where name = ? " +
                     "and breed=?"]
  }

}