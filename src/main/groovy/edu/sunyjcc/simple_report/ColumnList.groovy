package edu.sunyjcc.simple_report

/** This class describes the output columns for a report. */
public class ColumnList extends ArrayList<Column> implements Exportable {

  /** Return a list of exported values for all columns */
  def export() {
    this.collect{it.export()}
  }
}