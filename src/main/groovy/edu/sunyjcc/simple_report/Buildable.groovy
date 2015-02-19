package edu.sunyjcc.simple_report;

/** This interface allows a class to be built by the SimpleReportBuilder.
 */
public interface Buildable {
  /** Return a block of HTML text that tells us what we create when we build
   *  this object. */
  String        getBuildDocHtml();

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions();

  /** The source code used to create this object. It will be provided by
   *  the SimpleReportBuilder if a
   */
  String source;
}