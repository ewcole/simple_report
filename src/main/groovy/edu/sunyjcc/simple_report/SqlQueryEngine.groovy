package edu.sunyjcc.simple_report 

import groovy.sql.Sql

/** A query based on an SQL statement
 */
public class SqlQueryEngine extends QueryEngine {

  String queryEngineType = "sql"
  
  Sql sql

  /** The SQL language text of the database query. */
  String query

  /** The query after it's been processed */
  private String parsedQuery

  boolean init(HashMap args) {
    if (args.sql) {
      this.sql = args.sql
      assert this.sql
    }
    (args.keySet() - ["parsedQuery", "sql"]).each {
      this[it] = args[it];
    }
    return true;
  }

  def export() {
    return super.export() + [queryEngineType: this.queryEngineType,
                             class: this.getClass().name as String,
                             query: this.query]
  }

  /** List the columns that this report produces. */
  ArrayList getColumns() {
    []
  }


  /** Public hash-map constructor */
  public SqlQueryEngine(HashMap args) {
    init(args)
  }
}