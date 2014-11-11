package edu.sunyjcc.simple_report;

import java.util.*

/** An ordered list of parameters that can be used with the SimpleReports 
 *  system.
 */
public class ParamForm implements Buildable, Exportable, Runnable {
  // The parameters
  HashMap<String,Param> params = [:]
  private boolean isValid;

  /** Return the parameter items as a list */
  def export() {
    params.keySet().inject([:]) {
      map, paramName ->
        map[paramName] = this.params[paramName].export()
        return map
    }
  }

  @Override
  public ParamFormValue getParamFormValue() {
    new ParamFormValue(this)
  }
  // /** Return a HashMap with the keys being the names of the parameters 
  //  *  in the ParamForm and the values their current value. 
  //  */
  // HashMap getValues() {
  //   this.keySet().inject([:]) {
  //     valueMap, paramName ->
  //       def p = this[paramName]
  //       valueMap += [(paramName): (p.hasProperty('currentValue'))?p.currentValue:p]
  //       return valueMap
  //   }
  // }

  /** Access the isValid property */
  public boolean getIsValid() {
    return isValid;
  }

  /** Perform whatever initialization is needed for the parameters. */
  ParamForm init(HashMap args) {
    params.each {it.value.init(args)}
    return this
  }

  /** Test all parameters and see if they're OK. */
  ParamForm validate() {
    return this
  }
  
  @Override
  HashMap run(ParamFormValue paramFormValue) {
    this.getParamFormValue().run(paramFormValue)
  }
  // /** Look for matching parameters in the hash map
  //  *  and set the matching parameter values if possible. 
  //  */
  // ParamForm setValues(HashMap p) {
  //   // Create a new param map with lower-case keys 
  //   this.each {
  //     paramName, param ->
  //       def key = param.name
  //       if (p[key]) {
  //         param.value = p[key]
  //         this[param.name] = param
  //       }
  //   }
  //   return this
  // }
}