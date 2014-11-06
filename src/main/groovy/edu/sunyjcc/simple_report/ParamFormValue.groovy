package edu.sunyjcc.simple_report;

/** 
 *  A list of the values for a ParameterForm.
 */
public class ParamFormValue implements Exportable {
  /** The form that tells us what parameters are required. */
  ParamForm paramForm;
  HashMap<String, ParamValue> values

  /** Set the paramForm property from the given ParamForm variable.
   *  It initializes the values HashMap.
   *  @param paramForm The ParamForm that defines the Parameters you seek to collect.
   */
  ParamFormValue setParamForm(ParamForm paramForm) {
    println "in setParamForm(${paramForm.export()})"
    this.paramForm = paramForm;
    this.values = paramForm.params.inject([:]) {
      vals, paramFormEntry ->
        String paramName = paramFormEntry.key;
        Param param = paramFormEntry.value;
        ParamValue pv = new ParamValue(param)
        println "$paramName -> ${param.export()} -> ${pv.export()}"
        vals[paramName] = pv;
        return vals
    }
    println "this.values=${this.values}"
    assert this.values
    return this
  }

  /** Get the value of a parameter (this enables us to reference the parameter
   *  by dot notation or brackets).
   *  @param s The name of a parameter from the paramForm object.
   */
  public ParamValue get(String s) {
    assert values
    values[s]
  }

  /** Set the value of a parameters
   *  @param s The name of the parameter from the ParamForm object.
   */
  public ParamValue put(String s, ParamValue v) {
    assert values;
    assert values[s];
    values[s] = v;
  }

  // /** Set the value of a parameters
  //  *  @param s The name of the parameter from the ParamForm object.
  //  */
  // public ParamValue put(String s, String v) {
  //   assert values;
  //   assert values[s];
  //   values[s].currentValue = v;
  // }

  /** Initialize all contained objects with the given arguments
   *  @param args Data used for initialization.  This might contain 
   *              a database connection or other info.
   */
  public ParamFormValue init(HashMap args) {
    paramForm.init(args);
    values.each {
      key, val ->
        val.init(args);
    }
    return this
  }

  /** Construct this from a ParamForm object */
  public ParamFormValue(ParamForm paramForm) {
    setParamForm(paramForm)
  }

  @Override
  public def export() {
    def pf = paramForm.export();
    values.each {
      paramName, paramValue ->
        if (pf[paramName]) {
          pf[paramName] = paramValue.export()
        }
    }
    return pf;
  }

  /** Return a HashMap with the keys being the names of the parameters
   *  and the values their current value.
   */
  public HashMap getValues() {
    values.inject([:]) {
      pMap, param ->
        println "param.key=${param.key}, param.value=${param.value}"
        pMap[param.key] = "${param.value.value}" as String
        return pMap;
    }
  }
  
  /** Return a HashMap with the keys being the names of the parameters
   *  and the values their current value.
   */
  public ParamFormValue SetValues(HashMap<String,Object> v) {
    v.each {
      paramName, paramValue ->
        assert values[paramName]
        values[paramName].value = paramValue;
    }
    return this
  }
  
}