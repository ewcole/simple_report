package edu.sunyjcc.simple_report 

/** This class provides functions that generate the data for the report.
 *  You build an instance of this class into a report.
 */
public class CsvQueryEngine extends QueryEngine implements Buildable {

  String getBuildDocHtml() {
    "Change me."
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {[:]}


  File      file
  String    text
  ColumnList   columns
  ArrayList rows

  /* Inherited methods */


  QueryEngine init(HashMap args) {
    if (args?.file) {
      // If we are given a File, just copy it.  
      file = (args.file instanceof File)?
          // If we are given a File, just copy it.  
          args.file
          // Otherwise try to create a file object from it.
          :new File(args.file)
    } else if (args?.text) {
      this.text = args.text
    }
    return this;
  }

  def export() {
    def ex = [queryEngineType: 'csv',
              class: this.getClass().name as String]
    if (file) {
      ex += [file: f.getCanonicalFile().toString()]
    } else if (text?.size()) {
      ex += [text: text]
    }
    return ex
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

  /** read the CSV file input and set the local values */
  def parseCsv() {
    def csvText = (file?.exists())?file.text:text;
    parseCsvText(csvText)
  }

  /** List the columns that this report produces. */
  @Override
  ColumnList getColumns() {
    (ColumnList) parseCsv().columns
  }

  @Override
  ResultSet execute(HashMap params) {
    def data = parseCsv()
    def r = new ResultSet()
    r.columns = data.columns.collect{new Column([name: it, label: it])}
    r.rows = data.rows
    return r;
  }

  ResultSet execute() {
    execute([:])
  }

  CsvQueryEngine() {
    super()
  }

  CsvQueryEngine(HashMap args) {
    super()
    init(args)
  }

}