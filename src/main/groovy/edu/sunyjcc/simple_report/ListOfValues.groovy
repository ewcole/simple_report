package edu.sunyjcc.simple_report

import groovy.sql.Sql

/** A list of values to be used in a parameter definition */
public class ListOfValues implements Buildable {

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
    [sql: [desc: """A two-column SQL select statement to retrieve the list values."""],
     values: [desc: """A list of acceptable values for the parameter.  
                       Each item in the list must be a two-value list in which the first 
                       item is the value of the choice and the second is a description.
                       If an entry in the value list is not itself a list, the string value
                       will be used as both value and description."""]
    ]

  }

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

  static ListOfValues build(attributes) {
    return new ListOfValues(attributes)
  }

  public ListOfValues(HashMap attributes) {
    if (attributes.values) {
      valueClosure = {->attributes.values}
      exportClosure = {->
        [type: 'value_list',
         values: valueClosure()]
      }
    } else if (attributes.sql) {
      throw new Exception("not implemented")
    } else {
      throw new Exception("not implemented")
    }
  }
}