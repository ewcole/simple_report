package edu.sunyjcc.simple_report 

/** This class provides functions that generate the data for the report.
 *  You build an instance of this class into a report.
 */
public class QueryEngine implements Exportable {

  def export() {
    return [:]
  }

  /** List the columns that this report produces. */
  ArrayList getColumns() {
    []
  }

}