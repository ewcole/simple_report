package edu.sunyjcc.simple_report

/** A wrapper for a job engine closure that allows it to be added to a SimpleJob
 *  by the SimpleReportBuilder
 */
public class SimpleJobEngine implements Buildable {

  /** Provide a description for this object. */
  String getBuildDocHtml() {
    ("This creates an engine that can perform actions with side effects.")
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [closure: [desc: "A closure to execute the action."]]
  }

  Closure jobEngine;

  SimpleJobEngine init(HashMap args) {
    if (args.containsKey('closure')) {
      this.jobEngine = closure;
    }
  }

  def export() {
    [class: this.class]
  }
}
