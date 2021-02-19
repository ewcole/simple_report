package edu.sunyjcc.simple_report

import groovy.xml.*;

/** A wrapper for a job engine closure that allows it to be added to a SimpleJob
 *  by the SimpleReportBuilder
 */
public class SimpleJobEngine implements Buildable {

  /** The factory that created this object */
  ReportObjectFactory reportObjectFactory;
  
  /** Provide a description for this object. */
  String getBuildDocHtml() {
    def s = new StringWriter()
    def m = new MarkupBuilder(s);
    m.useDoubleQuotes = true
    m.div(class: "simple-job-engine-base-doc") {
      p("This creates an engine that can perform actions with side effects.");
    };
    return s.toString();
  }


    /** This code generates documentation about the closure 
   *  needed to generate the data for a ClosureQueryEngine.  */
  static String getQueryClosureDocs() {
    def s = new StringWriter()
    def m = new MarkupBuilder(s);
    m.useDoubleQuotes = true
    m.div(class: "closure-engine-doc") {
      p {
        mkp.yield """This is a closure that executes the action the job is 
           supposed to do."""
      }
      "h3"("Predefined Properties");
      dl(class: "properties") {
        dt("sql"); 
        dd("A groovy.sql.Sql database connection");
        // paramFormValue
        dt("params")
        dd{
          mkp.yield("A ")
          a(href: "file://x:doc/groovydoc/simple_report/edu/sunyjcc/simple_report/ParamFormValue.html",
            "ParamFormValue")
          mkp.yield " object, representing the parameters given by the user."
        }
        dt("markupBuilder");
        dd {
          mkp.yield("A groovy.xml.MarkupBuilder that writes to the response body text.");
          
        }
      }
    }
    return s.toString()
  }


  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [closure: [desc: getQueryClosureDocs()]]
  }

  Closure closure;

  SimpleJobEngine init(HashMap args) {
    if (args.containsKey('closure')) {
      this.jobEngine = closure;
    }
  }

  def export() {
    [class: this.class]
  }
}
