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

  /** A list of the parameters in the order they appear in the query */
  private ArrayList paramRefs

  /** The columns that this query will return.  It can be used to customize
   *  them if need be.
   */
  private ColumnList columns

  /** Read the SQL query string, extract the parameters, and return the 
   *  lists by name and place in the string.  Parameters are identified by 
   *  a colon followed by 1 or more alphabetic characters, underscores, or 
   *  digits, or, in other words, They match the regular expression 
   *  /:([a-zA-Z_0-9]+)/.
   *
   *  This function needs to be
   *  greatly enhanced before it's ready to be used in production.  In 
   *  particular, it cannot handle one-line comments and semicolons at the end
   *  of the query.
   */
  static HashMap parseSql(String query) {
    def paramRe = ~/:([a-zA-Z_0-9]+)/;
    [paramRefs:   (query =~ paramRe).collect { it[1].toLowerCase() },
     parsedQuery: query.replaceAll(paramRe, "?")]
  }

  boolean init(HashMap args) {
    if (args.sql) {
      this.sql = args.sql
      assert this.sql
    }
    (args.keySet() - ["parsedQuery", "sql"]).each {
      this[it] = args[it];
    }
    if (args.query) {
      def pq = parseSql(args.query)
      paramRefs = pq.paramRefs
      parsedQuery = pq.parsedQuery
    }
    return true;
  }

  def export() {
    return super.export() + [queryEngineType: this.queryEngineType,
                             class:           this.getClass().name as String,
                             query:           this.query,
                             parsedQuery:     this.parsedQuery,
                             paramRefs:       this.paramRefs,]
  }

  /** List the columns that this report produces. */
  ColumnList getColumns() {
    (ColumnList)[]
  }
  
  Closure getColumns = {
    meta ->
      println meta
      
  }


  ResultSet execute(ParamList params) {
    // This returns an empty result set.
    return new ResultSet(columns: new ColumnList(), rows: [])
    assert this.sql instanceof groovy.sql.Sql
    assert this.parsedQuery.size() > 0
    
  }

  /** Public hash-map constructor */
  public SqlQueryEngine(HashMap args) {
    init(args)
  }
}