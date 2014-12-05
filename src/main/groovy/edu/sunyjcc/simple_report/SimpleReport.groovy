package edu.sunyjcc.simple_report;

/** A report that can be defined in a small Groovy script, producing an 
 *  output
 */
public class SimpleReport implements Exportable {

  /* Report-level properties *************/
  /** The report name */
  String name    = "";

  /** The report's title */
  String title   = "";

  /** The report version */
  String version = "";

  /** A description of the report */
  String description;

  
  /** The active parameters for the report
   */
  private ParamForm params

  /** The columns that this report will produce */
  private ColumnList columns

  /** The engine that will power the report */
  private QueryEngine queryEngine

  /* End of report-level properties *************/


  public void setReportParams(ArrayList l) {
    // Don't let outsiders mess with params.
  }

  SimpleReport init(HashMap args) {
    assert queryEngine
    if (queryEngine) {
      queryEngine.init(args)
    }
    return this
  }

  /** Return a HashMap representing this report object. */
  def export() {
    [name: this.name,
     title: this.title,
     version: this.version,
     description: this.description,
     params: this.params?.export(),
     columns: this.columns?.export(),
     queryEngine: this.queryEngine?.export(),
    ]
  }

  /** Add a parameter to the report 
   *  @param p This parameter will be added to the end of the list.
   */
  public void addParam(Param p) {
    if (!params) {
      this.params = new ParamForm()
    }
    params.params[p.name] = p
  }

  /** Copies values from arguments into hashmap
   *  @param params A hash map that contains the name
   *                and version of the report.
   */
  private void newReport(HashMap params) {
    if (params.name) {
      this.name = params.name
    }
    if (params.version) {
      this.version = params.version
    }
    if (params.title) {
      this.title = params.title
    }
  }

  /** Create an unnamed report. Calls newReport() to do its stuff.
   */
  public SimpleReport() {
    newReport([version: '0.0.0'])
  }

  /** Create a report with values given in a hash map
   *  @param params A hash map that contains the name
   *                and version of the report.
   */
  public SimpleReport(HashMap params) {
    newReport(params);
  }
 
  /** Execute the report and return the result. */
  public ResultSet execute(HashMap params) {
    this.params.setParamValues(params)
    queryEngine.execute(this.params)
  }
 
}