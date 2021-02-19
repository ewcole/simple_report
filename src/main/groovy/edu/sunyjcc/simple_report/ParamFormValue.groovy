package edu.sunyjcc.simple_report;
import groovy.json.*
import groovy.xml.*
/** 
 *  A list of the values for a ParameterForm.
 */
public class ParamFormValue implements Exportable, Runnable {
  /** The form that tells us what parameters are required. */
  ParamForm paramForm;
  HashMap<String, ParamValue> privateValues

  public HashMap<String, ParamValue> getValues() {
    if (!privateValues) {
      privateValues = [:]
    }
    def sp = this.systemParams?.list().collect {
      key, value ->
      [name: key, value: value]
    }
    if (sp) {
      return sp.inject(privateValues) {
        valmap, sysParam ->
        valmap[sysParam.name] = sysParam.value;
        return valmap;
      }
    } else {
      return privateValues
    }
  }

  public HashMap<String, ParamValue> getNonSystemValues() {
    if (!privateValues) {
      privateValues = [:]
    }
    return privateValues
  }

  public void setValues() {
    assert 1 == 2
  }

  /** Set the paramForm property from the given ParamForm variable.
   *  It initializes the values HashMap.
   *  @param paramForm The ParamForm that defines the Parameters you seek to collect.
   */
  ParamFormValue setParamForm(ParamForm paramForm) {
    //println "in setParamForm(${paramForm.export()})"
    this.paramForm = paramForm?:new ParamForm();
    def reportValues = this.paramForm.params.inject([:]) {
      vals, paramFormEntry ->
        // paramForm.params is a HashMap
        String paramName = paramFormEntry.key;
        Param param = paramFormEntry.value;
        ParamValue pv = new ParamValue(param)
        //println "** $paramName -> ${param.export()} -> ${pv.export()}"
        vals[paramName] = pv;
        return vals
    }?:[:]
    def ce = getClientEnv();
    def systemParams = ce?.systemParams?.list().collect {
      [name: it.key, value: it.value.getParamValue()]
    }?:[]
    println "systemParams=${systemParams}"
    this.privateValues = systemParams.inject(reportValues) {
      rv, syprm ->
      rv[syprm.name] = syprm.value;
      return rv;
    };
    println "this.privateValues=${this.privateValues}"
    println "this.privateValues keys =${this.privateValues.keySet()}"
    assert this.privateValues != null
    return this
  }

  /** Get the value of a parameter (this enables us to reference the parameter
   *  by dot notation or brackets).
   *  @param s The name of a parameter from the paramForm object.
   */
  public ParamValue getValue(String s) {
    assert this.privateValues
    def v = this.privateValues[s]
    assert v instanceof ParamValue
    return v
  }

  public ClientEnv getClientEnv() {
    this.paramForm?.reportObjectFactory?.clientEnv
  }
  
  public SystemParams getSystemParams() {
    SystemParams spf = (clientEnv?.systemParams)?:new SystemParams()
    // assert spf instanceof SystemParams
    // spf.systemParams;
  }

  public void setSystemParams() {
    // do nothing - parameters are read-only.
  }

  /** Set the value of a parameters
   *  @param s The name of the parameter from the ParamForm object.
   */
  public ParamValue setValue(String s, ParamValue v) {
    assert privateValues;
    assert privateValues[s];
    assert privateValues[s] instanceof ParamValue
    assert v.value
    privateValues[s].setValue(v.value);
    assert privateValues[s].value == v.value
  }

  /** Set the value of a parameters
   *  @param s The name of the parameter from the ParamForm object.
   */
  public ParamValue setValue(String s, String v) {
    assert privateValues;
    assert privateValues[s];
    assert privateValues[s] instanceof ParamValue
    privateValues[s].setValue(v);
  }

  /** Set the value of a parameters
   *  @param s The name of the parameter from the ParamForm object.
   */
  public ParamValue setValue(String s, Object v) {
    assert privateValues;
    assert privateValues[s];
    assert privateValues[s] instanceof ParamValue
    privateValues[s].setValue(v);
  }

  /** Initialize all contained objects with the given arguments
   * 
   *  @param args Data used for initialization.  This might contain a 
   *              database connection or other info.
   */
  public ParamFormValue init(HashMap args) {
    paramForm.init(args);
    privateValues.each {
      key, val ->
        val.init(args);
    }
    return this
  }

  /** Construct this from a ParamForm object */
  public ParamFormValue(ParamForm paramForm) {
    setParamForm(paramForm)
  }

  /** Construct this from a SimpleReport object */
  public ParamFormValue(SimpleReport simpleReport) {
    setParamForm(simpleReport.params)
  }

  @Override
  public def export() {
    def pf = paramForm.export();
    privateValues.each {
      paramName, paramValue ->
        if (pf[paramName]) {
          pf[paramName] = paramValue.export()
        }
    }
    return pf;
  }

  public String toJson() {
    def j = new JsonOutput()
    def t = this.export()
    def s = new StringWriter()
    s.println "{"
    s.println (t.keySet().collect {
                 "\"$it\": ${privateValues[it].toJson()}"
               }.join(",\r\n"))
    s.println "}"
    return s.toString()
  }

  /** Return a HashMap with the keys being the names of the parameters
   *  and the privateValues their current value.
   */
  public HashMap getValueMap() {
    def vm = privateValues.inject([:]) {
      pMap, param ->
        println "param.key=${param.key}, param.value=${param.value}"
        pMap[param.key] = param.value.value
        return pMap;
    }
    // Now add the system parameters to the value map
    this.systemParams.list().each {
      key, param ->
      vm["${key}"] = param.value;
    }
    return vm
  }
  
  /** Copy the values  from the HashMap object given to us.  
   *  It will ignore parameters not in this object.
   *  @param v A Map with parameter names for keys.
   */
  public ParamFormValue setParamValues(HashMap v) {
    println "in setParamValues(HashMap<String,Object> $v)"
    v.each {
      String paramName, paramValue ->
        println "....paramName = $paramName; paramValue=$paramValue"
        if (privateValues[paramName]) {
          assert this.privateValues[paramName] instanceof ParamValue
          privateValues[paramName].setValue(paramValue)
          assert privateValues[paramName].value == paramValue
          println "privateValues[$paramName] == ${privateValues[paramName].export()}"
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
        if (privateValues[paramName]) {
          privateValues[paramName].setValue(paramValue);
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
        out.print(this.toJson());
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
            this.valueMap.each {
              key, value ->
                tr {
                  td(class: "parameterName", key);
                  td(class: "parameterValue", "${value.value}");
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
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, Writer out) {
    println "in ParamFormValue.run($outputFormat, $paramFormValue, out)"
    if (runFunctions.containsKey(outputFormat.desc)) {
      println "....ready to run"
      //this.setParamValues(paramFormValue)
      println "....after setParamValues()"
      def f = runFunctions[outputFormat.desc]
      return f(out);
    } else {
      return false
    }
  }

  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   *  @return Returns true if successful, false otherwise.
   */
  @Override
  boolean run(OutputFormat outputFormat, ParamFormValue paramFormValue, OutputStream out) {
    run(outputFormat, paramFormValue, new BufferedWriter(new OutputStreamWriter(out)));
  }
  /** Run the runnable object, writing its output data to the stream you 
   *  provide.
   *  @param out An output stream that will hold the results of your run.
   *  @return Returns true if successful, false otherwise.
   */
  boolean run(OutputFormat outputFormat, Writer out) {
    println "in ParamFormValue.run(${outputFormat.code}, out)"
    if (runFunctions.containsKey(outputFormat.code)) {
      println "....ready to run"
      return runFunctions[outputFormat.code](out);
    } else {
      return false
    }
  }

}
