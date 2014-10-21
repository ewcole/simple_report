package edu.sunyjcc.simple_report;

import java.util.*

/** An ordered list of parameters that can be used with the SimpleReports 
 *  system.
 */
public class ParamForm extends HashMap<String, Param> 
                       implements Buildable, Exportable {
  /** Return the parameter items as a list */
  def export() {
    this.keySet().inject([:]) {
      map, paramName ->
        map[paramName] = this[paramName].export()
        return map
    }
  }

  /** Return a HashMap with the keys being the names of the parameters 
   *  in the ParamForm and the values their current value. 
   */
  HashMap getValues() {
    this.keySet().inject([:]) {
      valueMap, paramName ->
        def p = this[paramName]
        valueMap += [(paramName): (p.hasProperty('currentValue'))?p.currentValue:p]
        return valueMap
    }
  }

  /** Perform whatever initialization is needed for the parameters. */
  ParamForm init(HashMap args) {
    this.each {it.init(args)}
    return this
  }

  /** Look for matching parameters in the hash map
   *  and set the matching parameter values if possible. 
   */
  ParamForm setValues(HashMap p) {
    // Create a new param map with lower-case keys 
    this.each {
      paramName, param ->
        def key = param.name
        if (p[key]) {
          param.value = p[key]
          this[param.name] = param
        }
    }
    return this
  }
}