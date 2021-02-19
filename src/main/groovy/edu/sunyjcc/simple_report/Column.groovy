package edu.sunyjcc.simple_report

public class Column implements Exportable {
  /** The column name */
  String name
  /** The suggested label for displaying the field. */
  String label
  /** A one-line description of the field */
  String description
  /** What is the suggested width (in characters) for printing this field */
  Long   displaySize
  /** What is the name of the class that you will get from the query? */
  String columnClassName
  /** How many characters or digits is the field? */
  Long precision
  /** How many decimal places, if number; else 0 */
  Long scale
  /** Tell us about this column. */
  String comments

  def export() {
    [class: this.getClass().name,
     name:  this.name,
     label: this.label,
     description: this.description,
     columnClassName: this.columnClassName,
     displaySize: this.displaySize,
     precision:   this.precision,
     scale:       this.scale,
     comments: this.comments,]
  }

  // public Column(String s) {
  //   this.name = s;
  //   this.label = s.toUpperCase().replaceAll(/_/,' ');
  // }
}
