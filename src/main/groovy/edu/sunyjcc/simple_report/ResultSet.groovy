package edu.sunyjcc.simple_report

public class ResultSet {
  ColumnList columns
  List<HashMap> rows

  def export() {
    [columns: columns.export(),
     rows:    rows]
  }
}