package edu.sunyjcc.simple_report

enum OutputFormat {
  
    pdf("PDF", "Adobe Acrobat", true, "application/pdf"),
    html("HTML", "HTML Web Page", false, "text/html"),
    xml("XML", "XML Data", false, "text/xml"),
    csv("CSV", "CSV (for spreadsheets)", false, "application/csv"),
    xls("XLS", "Excel 97-2007", true, "application/vnd.ms-excel"),
    rtf("RTF", "Rich Text", true, "application/rtf"),
    text("TEXT", "Plain Text", false, "text/plain", "txt"),
    odt("ODT", "Open Office Word", true, "application/odt"),
    ods("ODS", "Open Office Spreadsheet", true, "application/ods"),
    docx("DOCX", "Microsoft Word", true, "application/msword"),
    xlsx("XLSX", "Microsoft Excel", true, "application/vnd.ms-excel"),
    pptx("PPTX", "Microsoft Powerpoint", true, "application/vnd.ms-powerpoint"),
    // Not supported by Jasper Reports.
    json("JSON", "JSON Data", false, "application/json"),

    OutputFormat(String code, String desc, 
                 boolean isBinary,
                 String mimeType, String extension = '') {
      this.code = code
      this.desc = desc
      this.isBinary = isBinary
      this.mimeType = mimeType
      this.extension = (extension?.size())?extension:(code.toLowerCase());
    }

    String code;
    String desc;
    boolean isBinary;
    String mimeType;
    String extension

    String toString() {
      this.desc
    }
}