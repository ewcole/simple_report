package edu.sunyjcc.simple_report

enum OutputFormat {
  
    pdf("PDF", "Adobe Acrobat", "application/pdf"),
    html("HTML", "HTML Web Page", "text/html"),
    xml("XML", "XML Data", "text/xml"),
    csv("CSV", "Comma-Separated Values (for spreadsheets)", "application/csv"),
    xls("XLS", "Excel 97-2007", "application/vnd.ms-excel"),
    rtf("RTF", "Rich Text", "application/rtf"),
    text("TEXT", "Plain Text", "text/plain", "txt"),
    odt("ODT", "Open Office Word", "application/odt"),
    ods("ODS", "Open Office Spreadsheet", "application/ods"),
    docx("DOCX", "Microsoft Word", "application/msword"),
    xlsx("XLSX", "Microsoft Excel", "application/vnd.ms-excel"),
    pptx("PPTX", "Microsoft Powerpoint", "application/vnd.ms-powerpoint"),
    // Not supported by Jasper Reports.
    json("JSON", "JSON Data", "application/json"),

    OutputFormat(String code, String desc, String mimeType, String extension = '') {
      this.code = code
      this.desc = desc
      this.mimeType = mimeType
      this.extension = (extension?.size())?extension:(code.toLowerCase());
    }

    String code;
    String desc;
    String mimeType;
    String extension

    String toString() {
      this.desc
    }
}