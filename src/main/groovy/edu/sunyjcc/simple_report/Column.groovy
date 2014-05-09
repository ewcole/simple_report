package edu.sunyjcc.simple_report

public class Column implements Exportable {

  String name
  String label
  String description
  String comments

  def export() {
    [class: this.getClass().name,
     name:  this.name,
     label: this.label,
     description: this.description,
     comments: this.comments]
  }
}