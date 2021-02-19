package edu.sunyjcc.simple_report

public class ResultSet {
  ColumnList columns
  List<HashMap> rows

  def export() {
    [columns: columns.export(),
     rows:    rows]
  }

  /** Zero-argument constructor will create one with empty colums
   *  and rows.
   */
  public ResultSet() {
    columns = new ColumnList()
    rows    = []
  }
}
