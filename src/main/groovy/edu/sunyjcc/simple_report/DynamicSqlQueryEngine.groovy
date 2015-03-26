
package edu.sunyjcc.simple_report 

import groovy.sql.Sql

/** A query based on an SQL statement
 */
public class DynamicSqlQueryEngine extends QueryEngine implements Buildable {

  String getBuildDocHtml() {
    """This creates a dynamic SQL query to be used in a report.  
       The build parameter is a closure that returns a 
       String containing the SQL query at run-time.  This closure will
       be called with a HashMap
    """
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [sql_closure:    [desc: """A closure that takes a map of parameters and returns the text of an SQL select statement.  
                          Parameters can be referenced within the text of the closure by writing ':' followed by
                          the name of the parameter.""",
                 required: true]]

  }

  /** The source code used to create this object */
  String source;

  String queryEngineType = "dynamic_sql"
  
  Sql sql

  /** The SQL language text of the database query. */
  def sqlClosure

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
    (args.keySet() - ["sqlClosure"]).each {
      if (this.hasProperty(it)) {
        this[it] = args[it];
      }
    }
    return this;
  }

  def export() {
    return super.export() + [queryEngineType: this.queryEngineType,
                             class:           this.getClass().name as String]
  }

  /** List the columns that this report produces. */
  ColumnList getColumns() {
    columns
  }
  
  /** Get the column information from the query execution. */
  Closure getColumnMetadata = {
    java.sql.ResultSetMetaData meta ->
      columns = columns?:new ColumnList()
      def cols = meta.collect {
        new Column(name:            it.columnName,
                   label:           it.columnLabel,
                   displaySize:     it.columnDisplaySize,
                   columnClassName: it.columnClassName,
                   precision:       it.precision,
                   scale:           it.scale)
      }
      println "cols.class = ${cols.getClass()}"
      columns << cols
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
        map[pv.key] = pv.value.value
        map
    }
    return execute(paramVals?:[:])
  }

  /** Show the SQL query that will be executed for the given parameters. */
  public String getSqlText(HashMap params) {
    sqlClosure(params)
  }

  /** Execute the query with the given parameter list.
   *  @param params The parameters we will use in this query.
   */
  ResultSet execute(HashMap params) {
    assert this.sql instanceof groovy.sql.Sql
    println "DynamicSqlQueryEngine.execute($params)"
    String sqlText = getSqlText(params)
    def parsedSql = parseSql(sqlText)
    String parsedQuery = parsedSql?.parsedQuery
    assert parsedQuery?.size() > 0
    println "SQL query: $parsedQuery"
    def rows = []
    HashMap paramVals = params.subMap(parsedSql.paramRefs?.unique())?:[:]
    if (paramVals.keySet().size()&& parsedSql?.paramRefs?.size()) {
      rows = sql.rows(paramVals, parsedQuery, getColumnMetadata)
    } else {
      rows = sql.rows(parsedQuery, getColumnMetadata)
    }
    return new ResultSet(columns: columns, rows: rows)
  }

  /** Public hash-map constructor */
  public DynamicSqlQueryEngine(HashMap args) {
    assert args.sql_closure
    assert args.sql_closure instanceof Closure
    sqlClosure = args.sql_closure
    init(args)
  }

  /** Public hash-map constructor */
  public DynamicSqlQueryEngine() {
  }
}