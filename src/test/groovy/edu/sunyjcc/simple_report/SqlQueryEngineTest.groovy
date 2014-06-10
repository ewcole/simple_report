package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport
import groovy.sql.Sql

/** Test the SqlQueryEngine class. */
public class SqlQueryEngineTest extends GroovyTestCase {

  // /** Connect to one of the JCC databases using the familiar connection string. 
  //  *  @param connectString A connection string in the form
  //  *                       user/password@database
  //  */
  // static Sql staticConnect(String connectString) {
  //   def matcher = (connectString =~ /(.*?)\/(.*?)@(.*)/);
  //   if (matcher.matches()) {
  //     String username = matcher[0][1];
  //     String password = matcher[0][2];
  //     String db = (matcher[0][3]).toUpperCase();
  //     String server = "jsrvbannertst01";
  //     matcher = (db =~ /^JCCSCT$/);
  //     if ( matcher.matches() ) {
  //       server = "jsrvbanner01";
  //     }
  //     String connector = "jdbc:oracle:thin:@" + server + ":1521:" + db;
  //     return Sql.newInstance(connector, username, password, 'oracle.jdbc.OracleDriver');
  //   }
  //   return null;
  // }

  // Sql sqlConnection

  // /** Retrieve the SQL login from the environment */
  // Sql getSql() {
  //   if (!sqlConnection) {
  //     String userid   = System.env.grails_user
  //     String password = System.env.grails_password
  //     String db       = System.env.grails_db
  //     assert userid   != ""
  //     assert password != ""
  //     assert db       != ""
  //     sqlConnection = staticConnect("$userid/$password@$db")
  //   }
  //   return sqlConnection
  // }

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
                  parsedQuery: "select puppies from daisy_hill where name = :snoopy " +
                     "and breed=:beagle"]
  }

  void testQuery() {
    println "**** testParseSqlNoParams ****************"
    //getSql()
    println ("*"*70)
    println ()
  }
}