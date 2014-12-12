package edu.sunyjcc.simple_report 

/** This class provides functions that generate the data for the report.
 *  You build an instance of this class into a report.
 */
public abstract class QueryEngine implements Exportable {

  String queryEngineType = "queryEngine"

  def export() {
    return [queryEngineType: this.queryEngineType, 
            class: this.getClass().name as String]
  }

  /** Initialize the query engine so that it will be able to get its data.
   *  The required arguments are probably different for each type of engine;
   *  For the SqlQueryEngine, for instance, it will include the connection 
   *  object. 
   *  @param args A HashMap that will include all of the information you need
   *              to connect to the data source behind the query engine.
   */
  QueryEngine init(HashMap args) {
    return this
  }

  /** List the columns that this report produces. */
  abstract ColumnList getColumns();

  ResultSet execute(ParamFormValue params) {
    execute(params.getValueMap())
  }

  abstract ResultSet execute(HashMap params);

}