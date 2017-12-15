package edu.sunyjcc.simple_report;

import groovy.xml.*

/** A report that can be defined in a small Groovy script, producing an
 *  output
 */
public class SimpleReportInstance implements Exportable, Runnable {
  /** A SimpleReport object*/
  SimpleReport report;

  /** The values of all parameters */
  ParamFormValue params;

  // Methods Required by Runnable interface

  /** Get a param form value for the object.*/
  ParamFormValue getParamFormValue() {
    params
  }

  String csvEscape(String v) {
    //    if (v =~ /,/) {
      '"' + v.replaceAll('"', '""') + '"'
    // } else {
    //   v
    // }
  }

  /** Create a new SimpleReportInstance for the report object.
   *  @param report The SimpleReport that this instance will wrap.
   */
  public SimpleReportInstance(SimpleReport report) {
    this.report = report
    this.params = report.getParamFormValue()
  }

  /** CLOB rows were not being handled correctly.  Get the String 
                  //    value to get their contents. */
  public def convColumnType(def columnVal) {
    if (columnVal) {
      // Check to see if columnVal has a stringValue() method.
      boolean isClob = (columnVal?.metaClass?.respondsTo(columnVal,'stringValue'))
      def v = isClob ? columnVal.stringValue() : columnVal;
    } else {
      return null;
    }
  }

  def runFunctions = [
    // JSON: {
    //   Writer out ->
    //     out.print(this.toJson());
    //     out.flush()
    //     return true;
    // },
    CSV: {
      Writer out, ResultSet resultSet ->
        //println "In SimpleReportInstance.runFunctions[CSV]()"
        def cols = resultSet?.columns?.list().collect {it.name}
        def columnHeaders = cols.collect {
          csvEscape(it)
        }
        out.println (columnHeaders.join(','))
        // Now print the data rows
        resultSet?.rows?.each {
          row ->
            out.println (
              cols.collect {
                val ->
                  def v = convColumnType(row[val])?:''
                  csvEscape("${(v)?:''}")
              }.join(','));
        }
        out.flush()
        return true;
    },
    HTML: {
      Writer out, ResultSet resultSet ->
        //println "In SimpleReportInstance.runFunctions[HTML]()"
        def m = new MarkupBuilder(out);
        m.setDoubleQuotes(true)
        m.div(class: "simple_report $report.name") { 
          h1(this.report.title);
          table {
            thead {
              tr {
                resultSet?.columns?.each {
                  c ->
                    th(class: "${c.name}", tooltip: "${c.description}",
                       "${c.label}");
                }
              }
            }
            tbody {
              def cols = resultSet?.columns?.list().collect {it.name}
              resultSet?.rows?.eachWithIndex {
                row, i ->
                  tr(class: "data ${(i%2)?'even':'odd'}") {
                    cols.each {
                      // CLOB rows were not being handled correctly.  Get the String 
                      //    value to get their contents.
                      def datum = convColumnType(row[it])?:''
                      td(class: "$it", "${datum}")
                    }

                  }
              }
            }
          }
        }
        out.flush()
        return true;
    },
    // Turn off text output for now.  It was only put in production by accident.
    // TEXT: {
    //   Writer out, ResultSet resultSet ->
    //     //println "running TEXT"
    //     def rpad = {
    //       String text, Long size ->
    //         Long textSize = text?.size();
    //         Long padding = (size?:0) - textSize// (size?:0) - text?.size()
    //         text + (" " * ((padding>0)?padding:0))
    //     }
    //     def colHeaders = resultSet.columns.list().inject([]) {
    //       h, col ->
    //         assert col.name
    //         // assert col.displaySize
    //         h << rpad(col.name, (col.displaySize?:0))
    //         h
    //     }
    //     //println('After colHeaders');
    //     out.println ""
    //     out.println colHeaders.join(" ")
    //     out.println colHeaders.collect { "-" * it?.size() }.join(" ")
    //     resultSet.rows.each {
    //       row ->
    //         def lineText =  resultSet.columns.columnNames.collect {
    //           resultSet.columns.columns[it]
    //         }.collect {
    //           col ->
    //             Long colSize = (col.name.size() > (col.displaySize?:0))?col.name.size():col.displaySize
    //             rpad(row[col.name] as String, colSize)
    //         }.join(" ")
    //         out.println lineText;
    //         //println lineText;
    //     }
    //     out.println "${resultSet.rows.size()} rows selected."
    //     out.flush();
    //     return true;
    // }
  ]
  

  /** Get a list of the supported output types */
  @Override
  ArrayList<OutputFormat> getOutputFormats() {
    runFunctions.keySet().collect {OutputFormat[it.toLowerCase()]}
  }

  /** Run the runnable object, writing its output data to the stream you
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  @Override
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue,
              Writer out) {
    def oFm = outputFormat.code
    assert runFunctions[oFm]
    if (runFunctions[oFm]) {
      def rs = report.execute(paramFormValue)
      return runFunctions[oFm](out, rs);
    }
    return false
  }

  /** Run the runnable object, writing its output data to the stream you
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  @Override
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue,
              OutputStream out) {
    run(outputFormat, paramFormValue, new BufferedWriter(new OutputStreamWriter(out)));
  }

  /** Run the runnable object, writing its output data to the stream you
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   */
  boolean run(OutputFormat outputFormat, Writer out) {
    run(outputFormat, this.params, out)
  }

  /** Is this object valid and ready to run? */
  @Override
  boolean validate() {
    params.validate()
  }

  /** Return a simple Groovy object with all the important information about
   *  the implementing object.
   */
  @Override
  def export() {
    def r = report.export()
  }

}
