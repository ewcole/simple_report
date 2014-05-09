package edu.sunyjcc.simple_report 

/** This class provides functions that generate the data for the report.
 *  You build an instance of this class into a report.
 */
public class CsvQueryEngine extends QueryEngine {

  File   file
  String text

  /* Inherited methods */


  boolean init(HashMap args) {
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
    return true;
  }

  def export() {
    def ex = [queryEngineType: 'csv',
              class: this.getClass().name as String]
    if (file) {
      ex += [file: f.getCanonicalFile().toString()]
    } else if (text.size()) {
      ex += [text: text]
    }
  }

  /** List the columns that this report produces. */
  ArrayList getColumns() {
    []
  }
  
  CsvQueryEngine() {
    super()
  }

  CsvQueryEngine(HashMap args) {
    super()
    init(args)
  }

}