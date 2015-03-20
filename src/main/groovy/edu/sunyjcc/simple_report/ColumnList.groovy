package edu.sunyjcc.simple_report

/** This class describes the output columns for a report. */
public class ColumnList implements Exportable {

  ArrayList columnNames = []
  HashMap   columns     = [:]

  /** Return an array list with all of the columns in order. */
  def list() {
    columnNames.collect {columns[it]}
  }

  /** Override the '<<' operator; add to the end of the column list,
   *  unless it already exists.  If it exists, then do nothing. 
   */
  ColumnList leftShift(Column c) {
    add(c)
    return this
  }

  /** Iterate through each column and execute the closure */
  ArrayList each(Closure c) {
    list().each(c)
  }

  Long size() {
    return columns?.size()
  }

  /** Add */
  Column add(Column c) {
    assert c?.name?.size()
    String columnName = c.name.toLowerCase()
    Column c2 = columns[columnName];
    if (!columnNames.contains(columnName)) {
      columnNames << columnName
      columns[columnName] = c
    }
  }

  

  /** Return a list of exported values for all columns */
  def export() {
    this.columns.collect{it.value.export()}
  }

  /** Build a ColumnList from an ArrayList 
   *  @param c An ArrayList full of 
   */
  public ColumnList(ArrayList c) {
    c.each {
      if (it instanceof Column) {
        this.add(it)
      } else {
        this.add(new Column(it))
      }
    }
  }

  /** Build a ColumnList from another ColumnList */
  /* public ColumnList(ColumnList c) {
    c.each {
      columns.add(it)
    }
    }*/
}