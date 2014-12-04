package edu.sunyjcc.simple_report

enum OutputFormat {
  
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
    // Not supported by Jasper Reports.
    json("JSON", "application/json"),

    OutputFormat(String desc, String mimeType, String extension = '') {
      this.desc = desc
      this.mimeType = mimeType
      this.extension = (extension?.size())?extension:(desc.toLowerCase());
    }

    String desc;
    String mimeType;
    String extension

    String toString() {
      this.desc
    }
}