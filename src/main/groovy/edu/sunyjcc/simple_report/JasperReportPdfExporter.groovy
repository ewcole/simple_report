package edu.sunyjcc.simple_report;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
// import net.sf.jasperreports.engine.export.JRPdfExporter;

public class JasperReportPdfExporter extends JasperReportExporter {

  /** This is the output format that will be */
  OutputFormat outputFormat = OutputFormat.pdf;

  /** Export the printed report as a PDF file. */
  public void exportReport(JasperPrint jp, Writer out) {
    JasperExportManager.exportReportToPdfStream(jp, out);    
  }

  /** Export the printed report as a PDF file. */
  public void exportReport(JasperPrint jp, OutputStream out) {
    JasperExportManager.exportReportToPdfStream(jp, out);    
  }

  /** Zero-argument constructor. */
  public JasperReportPdfExporter() {
  }
}