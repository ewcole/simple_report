package edu.sunyjcc.simple_report

/** This class provides a set of parameters that will be added to all 
 *  parameter forms run by this module.  The parameters are stored 
 *  internally as closures
 */
public class SystemParams implements Iterable {
  //  def vars = new Expando();
  def params = new Expando();

  /** Add a system parameter where the value is provided by a 
   *    zero-parameter closure */
  def addSystemParam(String name, Closure action) {
    //def getterName = "get" + name.replaceAll(/^(.)/, '$1'.toUpperCase())
    String getterName = name;
    params."${getterName}" = new SystemParam(name: getterName,
                                             valueClosure: action,
                                             description:  name,
                                             label:   name);
  }

  /** Add a system parameter with the given constant value */
  def addSystemParam(String name, Object value) {
    String getterName = name;
    params."${getterName}" = new SystemParam(name: getterName,
                                             valueClosure: {-> value},
                                             description:  name,
                                             label:   name);
  }

  /** Add a system parameter with the given constant value */
  def addSystemParam(String name, SystemParam systemParam) {
    String getterName = name;
    params."${getterName}" = systemParam;
  }

  /** Add a system parameter with the given constant value */
  def addSystemParam(SystemParam systemParam) {
    String getterName = systemParam.name;
    assert getterName?.size();
    params."${getterName}" = systemParam;
  }
  
  def get(String name) {
    params."$name"?.value
  }

  /** Allow you to set a property using dot or bracket notation */
  def set(String name, Object value) {
    addSystemParam(name, value);
  }
  
  def list() {
    params.properties.collect {
      [name: it.key, value: it.value]
    }.inject([:]) {
      m, v ->
      m[v.name] = v.value;
      return m;
    }
  }

  ArrayList keySet() {
    params.properties.collect { it.key }
  }
  
  /** Zero-argument constructor */
  public SystemParams() {
    // println "zero-argument constructor"
  }

  /** Map constructor */
  public SystemParams(def m) {
    // println "map constructor"
    m.each {
      key, value ->
      // println "$key: $value"
      addSystemParam(key, value);
    }
  }

  public int size() {
    return this.params.properties.size()
  }

  java.util.Iterator iterator() {
    return this.params.iterator()
  }

}

