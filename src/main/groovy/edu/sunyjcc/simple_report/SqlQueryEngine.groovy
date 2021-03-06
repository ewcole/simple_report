
package edu.sunyjcc.simple_report 

import groovy.sql.Sql

/** A query based on an SQL statement
 */
public class SqlQueryEngine extends QueryEngine implements Buildable {

  /** The factory that created this object */
  ReportObjectFactory reportObjectFactory;
  
  String getBuildDocHtml() {
    "This creates an SQL query to be used in a report."
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [query:    [desc: "The text of an SQL select statement.  Use ':' to mark bind variables.",
                required: true]]

  }

  /** The source code used to create this object */
  String source;

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
  private ColumnList columns = (ColumnList)[]

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
     parsedQuery: query]
  }

  QueryEngine init(HashMap args) {
    if (args.sql) {
      this.sql = args.sql
      assert this.sql
    }
    (args.keySet() - ["parsedQuery", "sql"]).each {
      if (this.hasProperty(it)) {
        this[it] = args[it];
      }
    }
    if (args.query) {
      def pq = parseSql(args.query)
      paramRefs = pq.paramRefs
      parsedQuery = pq.parsedQuery
    }
    return this;
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
    columns
  }
  
  /** Get the column information from the query execution. */
  Closure getColumnMetadata = {
    java.sql.ResultSetMetaData meta ->
      columns = columns?:(new ColumnList(cols))
      def cols = meta.collect {
        new Column(name:            it.columnName,
                   label:           it.columnLabel,
                   displaySize:     it.columnDisplaySize,
                   columnClassName: it.columnClassName,
                   precision:       it.precision,
                   scale:           it.scale)
      }
      columns << cols
      println "cols.class = ${cols.getClass()}"
  }

  /** Execute without passing parameters. The query will fail if there are any bind variables in it.
   */
  ResultSet execute() {
    // assert this.sql instanceof groovy.sql.Sql
    // assert this.parsedQuery.size() > 0
    // def rows = sql.rows(parsedQuery, getColumnMetadata)
    // return new ResultSet(columns: columns, rows: rows)
    return execute([:])
  }

  /** Execute the query with the given parameter list.
   *  @param params The parameters we will use in this query.
   */
  ResultSet execute(ParamFormValue params) {
    def paramVals = params?.getValues()?.inject([:]) {
      map, pv ->
      if (paramRefs.contains(pv.key)) {
        map[pv.key] = pv.value.value
      }
      map
    }
    return execute(paramVals?:[:])
  }

  /** Execute the query with the given parameter list.
   *  @param params The parameters we will use in this query.
   */
  ResultSet execute(HashMap params) {
    assert this.sql instanceof groovy.sql.Sql
    println "SqlQueryEngine.execute($params)"
    if (!parsedQuery) {
      parseSql(this.query)
    }
    assert this.parsedQuery?.size() > 0
    def rows = []
    HashMap paramVals = params?:[:]
    if (paramVals.keySet().size()) {
      rows = sql.rows(paramVals, parsedQuery, getColumnMetadata)
    } else {
      rows = sql.rows(parsedQuery, getColumnMetadata)
    }
    return new ResultSet(columns: columns, rows: rows)
  }

  /** Public hash-map constructor */
  public SqlQueryEngine(HashMap args) {
    assert args.query
    init(args)
  }

  /** Public hash-map constructor */
  public SqlQueryEngine() {
  }
}
