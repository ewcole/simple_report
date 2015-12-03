package edu.sunyjcc.simple_report;

import net.sf.jasperreports.engine.JasperPrint;

/** Instances of this class write a Jasper Report filled with
 *  data, and write out the report in a finished form.  There
 *  is one JasperReportExporter class per supported output format.
 */
public abstract class JasperReportExporter implements Exportable {

  /** This is the output format that will be produced by the 
   *  export function.
   */
  OutputFormat outputFormat;

  /** Export the printed report to the format associated with this object. */
  public abstract void exportReport(JasperPrint jp, Writer out);

  /** Export the printed report to the format associated with this object. */
  public abstract void exportReport(JasperPrint jp, OutputStream out);

  public export() {
    [jasperReportExporter: [format: outputFormat.code]]
  }
}