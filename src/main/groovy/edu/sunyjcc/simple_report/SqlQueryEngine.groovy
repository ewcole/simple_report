package edu.sunyjcc.simple_report 

import groovy.sql.Sql

/** This class provides functions that generate the data for the report.
 *  You build an instance of this class into a report.
 */
public class SqlQueryEngine extends QueryEngine {

  Sql sql
  
  def export() {
    return [:]
  }

  /** List the columns that this report produces. */
  ArrayList getColumns() {
    []
  }

}