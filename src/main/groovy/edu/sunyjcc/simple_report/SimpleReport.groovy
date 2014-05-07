package edu.sunyjcc.simple_report;

/** A report that can be defined in a small Groovy script, producing an 
 *  output
 */
public class SimpleReport {

  /* Report-level properties *************/
  /** The report name */
  String name    = ""
  /** The report version */
  String version = ""

  /** The active parameters for the report
   */
  private ArrayList reportParams = []

  public void setReportParams(ArrayList l) {
    // Don't let outsiders mess with reportParams.
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
  
}