package edu.sunyjcc.simple_report

enum JasperReportFormat {
  
    pdf("PDF", "application/pdf"),
    html("HTML", "text/html"),
    xml("XML", "text/xml"),
    csv("CSV", "application/csv"),
    xls("XLS", "application/vnd.ms-excel"),
    rtf("RTF", "application/rtf"),
    text("TEXT", "text/plain", ".txt"),
    odt("ODT", "application/odt"),
    ods("ODS", "application/ods"),
    docx("DOCX", "application/msword"),
    xlsx("XLSX", "application/vnd.ms-excel"),
    pptx("PPTX", "application/vnd.ms-powerpoint"),
    //json("JSON", "application/json"),

    JasperReportFormat(String desc, String mimeType, String extension = '') {
      this.desc = desc
      this.mimeType = mimeType
      this.extension = (extension?.size())?extension:(desc.toLowerCase());
    }

    String desc;
    String mimeType;
    String extension
}