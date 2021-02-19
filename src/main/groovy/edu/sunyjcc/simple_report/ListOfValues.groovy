package edu.sunyjcc.simple_report

import groovy.sql.Sql

/** A list of values to be used in a parameter definition */
public class ListOfValues implements Buildable {

  /** The factory that created this object */
  ReportObjectFactory reportObjectFactory;
  
  String getBuildDocHtml() {
    "This creates an SQL query to be used in a report."
  }

  // /** The values returned by this list. */
  // def values = []
  /** The SQL query that generates the values */
  def queryStr

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [query: [desc: """A two-column SQL select statement to retrieve the list values."""],
     values: [desc: """A list of acceptable values for the parameter.  
                       Each item in the list must be a two-value list in which the first 
                       item is the value of the choice and the second is a description.
                       If an entry in the value list is not itself a list, the string value
                       will be used as both value and description."""]
    ]

  }

  /** A name that can be given this list of values. */
  String name;

  /** A database connection to use for SQL queries */
  Sql sql;

  /** The source code used to create this object */
  String source;

  Closure exportClosure = {->[:]}
  def export() {
    exportClosure()
  }

  /** The function to return the values for the list of values. */
  Closure valueClosure = {-> []}

  /** Return a list with the acceptable values from either the query or the */
  def getValues() {
    valueClosure()
  }

  ListOfValues init(HashMap args) {
    if (args.sql) {
      this.sql = new Sql(args.sql)
    } 
    return this
  }
  
  /** Build a new list of values from the given attributes. */
  static ListOfValues build(attributes) {
    return new ListOfValues(attributes)
  }

  public ListOfValues(HashMap attributes) {
    init(attributes)
    if (attributes.values) {
      def vals = attributes.values.collect {
        val ->
            [value: val[0], desc: val[1]]
      }
      valueClosure = {-> vals}
      exportClosure = {->
        [type: 'value_list',
         values: valueClosure()]
      }
    } else if (attributes.query) {
      // Get the list of values from the SQL query.
      queryStr = attributes.query
      def colNames = []
      // Method to capture query columns from query metadata
      def captureColNames = {
        meta ->
          colNames = []
          def cc = meta.columnCount
          println "cc = $cc"
          colNames << meta.getColumnLabel(1) as String
          // If there's only one column, duplicate the value. 
          // Otherwise return the first and the second columns.
          if (cc >=2) {
            colNames << meta.getColumnLabel(2) as String
          } else {
            colNames << meta.getColumnLabel(1) as String
          }
      }
      valueClosure = {
        sql.rows(queryStr, captureColNames).collect {
          row ->
            def c1 = row[colNames[0]] as String
            def c2 = row[colNames[1]] as String
            [value: c1, desc: c2]
        }
      }
      exportClosure = { ->
        [type: "sql",
         query: queryStr]
      }
    }
  }

  /** Zero-argument constructor is required for documentation */
  public ListOfValues() {}
}
