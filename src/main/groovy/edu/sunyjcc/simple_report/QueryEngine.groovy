package edu.sunyjcc.simple_report 

/** This class provides functions that generate the data for the report.
 *  You build an instance of this class into a report.
 */
public class QueryEngine implements Exportable {

  def export() {
    return [type: "queryEngine", 
            class: this.getClass() as String]
  }

  /** Initialize the query engine so that it will be able to get its data.
   *  The required arguments are probably different for each type of engine;
   *  For the SqlQueryEngine, for instance, it will include the connection 
   *  object. 
   *  @param args A HashMap that will include all of the information you need
   *              to connect to the data source behind the query engine.
   */
  boolean init(HashMap args) {}

  /** List the columns that this report produces. */
  ArrayList getColumns() {
    []
  }

}