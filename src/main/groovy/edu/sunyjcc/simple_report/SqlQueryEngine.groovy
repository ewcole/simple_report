package edu.sunyjcc.simple_report 

import groovy.sql.Sql

/** A query based on an SQL statement
 */
public class SqlQueryEngine extends QueryEngine {

  Sql sql

  boolean init(HashMap args) {
    if (args.sql) {
      this.sql = args.sql
      assert this.sql
    }
    return true;
  }

  def export() {
    return [type: 'sql',
            class: this.getClass().name as String,
            sql: this.sql]
  }

  /** List the columns that this report produces. */
  ArrayList getColumns() {
    []
  }

}