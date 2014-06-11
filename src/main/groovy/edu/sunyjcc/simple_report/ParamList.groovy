package edu.sunyjcc.simple_report;

import java.util.*

/** An ordered list of parameters that can be used with the SimpleReports 
 *  system.
 */
public class ParamList extends HashMap<String, Param> implements Exportable {
  /** Return the parameter items as a list */
  def export() {
    this.keySet().inject([:]) {
      map, paramName ->
        map[paramName] = this[paramName].export()
        return map
    }
  }

  /** Return a HashMap with the keys being the names of the parameters 
   *  in the ParamList and the values their current value. 
   */
  HashMap getValues() {
    this.inject([:]) {
      valueMap, val ->
        valueMap += [(val.name): val.currentValue]
        return valueMap
    }
  }

  /** Perform whatever initialization is needed for the parameters. */
  ParamList init(HashMap args) {
    this.each {it.init(args)}
    return this
  }

  /** Look for matching parameters in the hash map
   *  and set the matching parameter values if possible. 
   */
  ParamList setValues(HashMap p) {
    // Create a new param map with lower-case keys 
    this.each {
      param ->
        def key = param.name
        if (p[key]) {
          param.value = p[key]
        }
    }
    return this
  }
}