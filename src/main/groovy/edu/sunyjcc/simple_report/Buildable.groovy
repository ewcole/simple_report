package edu.sunyjcc.simple_report;

/** This interface allows a class to be built by the SimpleReportBuilder.
 */
public interface Buildable {
  /** Return a block of HTML text that tells us what we create when we build
   *  this object. */
  String getBuildDocHtml()
}