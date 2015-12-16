package edu.sunyjcc.simple_report

import java.text.DecimalFormat

enum ParamType {
  string("STRING", 
         {it.toString()},
         "text"),
  number("NUMBER",
         {numStr ->
           if (numStr instanceof String) {
             def df = new DecimalFormat();
             df.parseBigDecimal = true;
             df.parse(numStr);
           } else {
             numStr
           }
         },
        "number"),
  // date("DATE"
  //      {dateStr ->
  //        def newDate = (dateStr instanceof java.util.Date)?dateStr:'';
  //      }
  //     ),
  // integer("INTEGER"),

  ParamType(String desc, Closure parse, String htmlInputType) {
    this.desc = desc
    this.parse = parse
    this.htmlInputType = htmlInputType
  }

  /** The description of this type, which will be used in the 
   *  SimpleReportBuilder. */
  private final String desc
  /** A closure that accepts a single string value and converts
   *  it into the type that this object represents.
   */
  Closure parse;
  /** The HTML5 type that should be used for INPUT elements in a 
   *  form.  For example, "number," "text," or "datetime."
   */
  private final String htmlInputType;

  public String desc() {return desc}
}
