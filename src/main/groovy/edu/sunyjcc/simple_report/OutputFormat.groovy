package edu.sunyjcc.simple_report

enum OutputFormat {
  
    pdf("PDF", "Adobe Acrobat", true, true, "application/pdf"),
    html("HTML", "HTML Web Page", false, false, "text/html"),
    xml("XML", "XML Data", false, true, "text/xml"),
    csv("CSV", "CSV (for spreadsheets)", false, true, "application/csv"),
    xls("XLS", "Excel 97-2007", true, true, "application/vnd.ms-excel"),
    rtf("RTF", "Rich Text", true, true, "application/rtf"),
    text("TEXT", "Plain Text", false, true, "text/plain", "txt"),
    odt("ODT", "Open Office Word", true, true, "application/odt"),
    ods("ODS", "Open Office Spreadsheet", true, true, "application/ods"),
    docx("DOCX", "Microsoft Word", true, true, "application/msword"),
    xlsx("XLSX", "Microsoft Excel", true, true, "application/vnd.ms-excel"),
    pptx("PPTX", "Microsoft Powerpoint", true, true, "application/vnd.ms-powerpoint"),
    // Not supported by Jasper Reports.
    json("JSON", "JSON Data", false, true, "application/json"),

    OutputFormat(String code, String desc, 
                 boolean isBinary,
                 boolean isStandalone,
                 String mimeType, String extension = '') {
      this.code = code
      this.desc = desc
      this.isBinary = isBinary
      this.isStandalone = isStandalone
      this.mimeType = mimeType
      this.extension = (extension?.size())?extension:(code.toLowerCase());
    }

    /** Code that identifies this output type */
    String code;
    /** A description of the output type */
    String desc;
    /** Is this a binary format? */
    boolean isBinary;
    /** True means that this should be delivered as a fully independent 
     *    document.  False means that it should be included in an existing
     *    HTML page.
     */
    boolean isStandalone;
    /** The mime type that should be declared in the HTML headers. */
    String mimeType;
    /** The extension that the filename should have.  It defaults to the
     *  lower case o fhte code.
     */
    String extension

    String toString() {
      this.desc
    }
}
