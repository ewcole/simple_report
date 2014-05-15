package edu.sunyjcc.simple_report;

import java.util.*

/** An ordered list of parameters that can be used with the SimpleReports 
 *  system.
 */
public class ParamList extends ArrayList<Param> implements Exportable {
  /** Return the parameter items as a list */
  def export() {
    this.collect {it.export()}
  }

  def getValues() {
    this.inject([:]) {
      valueMap, val ->
        valueMap += [(val.name): val.value]
        return valueMap
    }
  }
}