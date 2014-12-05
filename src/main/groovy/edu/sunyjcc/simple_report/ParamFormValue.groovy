package edu.sunyjcc.simple_report;
import groovy.json.*
import groovy.xml.*
/** 
 *  A list of the values for a ParameterForm.
 */
public class ParamFormValue implements Exportable, Runnable {
  /** The form that tells us what parameters are required. */
  ParamForm paramForm;
  HashMap<String, ParamValue> values

  /** Set the paramForm property from the given ParamForm variable.
   *  It initializes the values HashMap.
   *  @param paramForm The ParamForm that defines the Parameters you seek to collect.
   */
  ParamFormValue setParamForm(ParamForm paramForm) {
    //println "in setParamForm(${paramForm.export()})"
    this.paramForm = paramForm;
    this.values = paramForm.params.inject([:]) {
      vals, paramFormEntry ->
        // paramForm.params is a HashMap
        String paramName = paramFormEntry.key;
        Param param = paramFormEntry.value;
        ParamValue pv = new ParamValue(param)
        println "** $paramName -> ${param.export()} -> ${pv.export()}"
        vals[paramName] = pv;
        return vals
    }
    println "this.values=${this.values}"
    println "this.values keys =${this.values.keySet()}"
    assert this.values
    return this
  }

  /** Get the value of a parameter (this enables us to reference the parameter
   *  by dot notation or brackets).
   *  @param s The name of a parameter from the paramForm object.
   */
  public ParamValue getValue(String s) {
    assert this.values
    def v = this.values[s]
    assert v instanceof ParamValue
    return v
  }

  /** Set the value of a parameters
   *  @param s The name of the parameter from the ParamForm object.
   */
  public ParamValue setValue(String s, ParamValue v) {
    assert values;
    assert values[s];
    assert values[s] instanceof ParamValue
    assert v.value
    values[s].setValue(v.value);
    assert values[s].value == v.value
  }

  /** Set the value of a parameters
   *  @param s The name of the parameter from the ParamForm object.
   */
  public ParamValue setValue(String s, String v) {
    assert values;
    assert values[s];
    assert values[s] instanceof ParamValue
    values[s].setValue(v);
  }

  /** Initialize all contained objects with the given arguments
   * 
   *  @param args Data used for initialization.  This might contain a 
   *              database connection or other info.
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
  public HashMap getValueMap() {
    values.inject([:]) {
      pMap, param ->
        println "param.key=${param.key}, param.value=${param.value}"
        pMap[param.key] = "${param.value.value}" as String
        return pMap;
    }
  }
  
  /** Copy the values  from the HashMap object given to us.  
   *  It will ignore parameters not in this object.
   *  @param v A Map with parameter names for keys.
   */
  public ParamFormValue setParamValues(HashMap v) {
    println "in setParamValues(HashMap<String,Object> $v)"
    v.each {
      String paramName, paramValue ->
        if (values[paramName]) {
          assert values[paramName] instanceof ParamValue
          values[paramName].setValue(paramValue)
          assert values[paramName].value == paramValue
        }
    }
    return this
  }

  /** Copy the values for shared parameters from the ParamFormValue
   *  object given to us.  It will ignore parameters not in this object.
   *  @param v Another ParamFormValue object.
   */
  public ParamFormValue setParamValues(ParamFormValue v) {
    println "in setParamValues(ParamFormValue $v)"
    v.values.each {
      paramName, paramValue ->
        if (values[paramName]) {
          values[paramName].setValue(paramValue);
        }
    }
    return this
  }

  /* Methods required by the Runnable interface */

  @Override
  /** Get a param form value for the object.*/
  ParamFormValue getParamFormValue() {
    return this;
  }

  /** Validate the object and return true or false if params are good 
   *  @param args The new argument values (optional)
   */
  boolean checkValidity(HashMap args) {
    setParamValues(args);
    return true;
  }

  /** Is this valid? */
  boolean getIsValid() {
    return checkValidity([:]);
  }

  boolean validate() {
    checkValidity([:]);
  }

  def runFunctions = [
    JSON: {
      Writer out ->
        def j = new JsonOutput()
        out.print(j.toJson(this.export()));
        out.flush()
        return true;
    },
    HTML: {
      Writer out ->
        def m = new MarkupBuilder(out);
        m.setDoubleQuotes(true)
        m.table {
          thead {
            th("Parameter");
            th("Value");
          }
          tbody {
            this.values.each {
              key, value ->
                tr {
                  td(class: "parameterName", key);
                  td(class: "parameterValue", "$value.value");
                }
            }
          }
        }
        out.flush()
        return true;
    },
  ]

  /** Get a list of the supported output types */
  ArrayList<OutputFormat> getOutputFormats() {
    runFunctions.keySet().collect {OutputFormat[it.toLowerCase()]}
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   *  @return Returns true if successful, false otherwise.
   */
  @Override
  boolean run(OutputFormat outputFormat, Writer out) {
    if (runFunctions.containsKey(outputFormat.desc)) {
      return runFunctions[outputFormat.desc](out);
    } else {
      return false
    }
  }

}
