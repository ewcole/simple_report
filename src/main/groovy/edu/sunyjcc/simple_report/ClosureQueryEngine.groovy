package edu.sunyjcc.simple_report 

import groovy.sql.Sql
import groovy.xml.*

/** This is a data engine that executes a 
 *  You build an instance of this class into a report.
 */
public class ClosureQueryEngine extends QueryEngine implements Buildable, Exportable {

  /** A database connection */
  Sql sql;

  String queryEngineType = "closure"

  /** The factory that created this object */
  ReportObjectFactory reportObjectFactory;
  
  /** Produce documentation on how to build and use this class. */
  String getBuildDocHtml() {
    def s = new StringWriter()
    def m = new MarkupBuilder(s);
    m.useDoubleQuotes = true
    m.div(class: "closure-engine-base-doc") {
      p {
        mkp.yield "This creates a source of data that takes its values from a closure " +
          "defined at the time of creation. It can be called either with a " +
          "closure or a map value containing a closure.  The closure is described below " +
          "under the ";
          em(class: "source_code", "closure");
          mkp.yield "option below.";
      }
    }
    s.toString();
  }

  /** This code generates documentation about the closure 
   *  needed to generate the data for a ClosureQueryEngine.  */
  static String getQueryClosureDocs() {
    def s = new StringWriter()
    def m = new MarkupBuilder(s);
    m.useDoubleQuotes = true
    m.div(class: "closure-engine-doc") {
      p {
        mkp.yield """This is a closure that generates the data 
           that will be returned to the report.  It should have """
        em("no side effects")
        mkp.yield """, but do its work exclusively through calling the row
                       and column functions."""
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
        }
      h3("Predefined Methods");
      dl(class: "methods") {
        dt("column");
        dd {
          mkp.yield """Define an output column for the report.  It takes a map
                       with the following values"""
          dl(class: "map-values") {
            dt("name");
            dd("The column name");
            dt("label");
            dd("The suggested label for displaying the field.");
            dt("description");
            dd("A one-line description of the field");
            dt("displaySize");
            dd("What is the suggested width (in characters) for printing this field");
            dt("columnClassName");
            dd("What is the name of the class that you will get from the query?");
            dt("precision");
            dd("How many characters or digits is the field?");
            dt("scale");
            dd("How many decimal places, if number; else 0");
            dt("comments");
            dd("Tell us about this column.");
          }
        }
        dt("row");
        dd {
          mkp.yield """Add a row to the result set of the report.  
                       It takes a map with one entry for each column.
                       """
        }
      }
    }
    return s.toString()
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [closure: [desc: getQueryClosureDocs(),
              action: {Closure closure -> dataScript = closure},
             ]]
  }

  ColumnList columns
  ArrayList  rows

  Closure dataScript;

  /* Inherited methods */


  QueryEngine init(HashMap args) {
    if (args?.sql) {
      this.sql = args.sql
    } 
    return this;
  }

  /** read the CSV file input and set the local values */
  def parseCsvText(String csvText) {
    def lines = csvText.readLines()*.split(',');
    def columns = (lines?.size() > 0)?lines[0]:[]
    // println "columns=$columns"
    // println "lines=$lines"
    // println "lines.size()=${lines.size()}"
    def rows = (lines.size()<=1)?[]:(
      lines[1..-1].collect{
        line ->
          // println "line=$line"
          def m = [:]
          line.eachWithIndex {
            fieldVal, i ->
              m[columns[i]] = fieldVal
          }
          // println "m=$m"
          return m
      }
    )
    // println "rows=$rows"
    [columns: columns,
     rows: rows]
  }

  /** List the columns that this report produces. */
  @Override
  ColumnList getColumns() {
    return columns;
  }

  @Override
  public ResultSet execute(HashMap params) {
    assert dataScript;
    def rows = []
    ResultSet resultSet = new ResultSet();
    ColumnList cols = this.columns?:new ColumnList();
    // 1. Build the delegate that defines the classes and 
    //    properties the closure can call
    def model = new Expando(sql: sql);
    model.column = {args -> resultSet.columns << new Column(args)}
    model.row = {
      HashMap args -> 
        resultSet.rows << args
        args.keySet().collect {it}.each {
          resultSet.columns << it
        };
    }
    model.params = params?:[:]
    // 2. Call the closure with the pre-defined environment
    println "model=$model"
    dataScript.delegate = model;
    dataScript.resolveStrategy = Closure.DELEGATE_ONLY
    dataScript();
    // 3. Send the results back to the user.
    return resultSet;
  }

  public ResultSet execute() {
    this.execute([:])
  }

  /** Execute the query with the given parameter list.
   *  @param params The parameters we will use in this query.
   */
  ResultSet execute(ParamFormValue params) {
    def paramVals = params?.getValues()?.inject([:]) {
      map, pv ->
        map[pv.key] = pv.value.value
        map
    }
    return execute(paramVals?:[:])
  }

  public setValues(HashMap options) {
    options.each {
      opt ->
        if (buildOptions.containsKey(opt.key)) {
          buildOptions[opt.key].action(opt.value);
        }
    }
  }

  public ClosureQueryEngine() {
    super()
  }

  public ClosureQueryEngine(HashMap args) {
    super()
    setValues(args)
    init(args)
  }

  @Override
  public export() {
    def ex = [queryEngineType: queryEngineType,
              class: this.getClass().name as String]
    return ex
  }

}
