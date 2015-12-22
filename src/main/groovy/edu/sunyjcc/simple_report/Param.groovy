package edu.sunyjcc.simple_report;

/** A parameter definition to be used in a report or application invocation.
 */
public class Param implements Exportable, Buildable {

  String getBuildDocHtml() {
    "This creates a single parameter that will appear on a parameter form."
  }

  /** List the different options you can pass as parameters to the builder 
   *  method call for this class. */
  LinkedHashMap getBuildOptions() {
    [name:        [desc: """The name of the parameter.  This is the handle that will be used to 
                            reference it within SQL queries and other code, so it should be a 
                            valid variable name in SQL, Groovy, and other formats; please use only 
                            alphanumeric characters and the underscore ("_").""",
                   required: true],
     type:        [desc: "The type of the parameter.  The supported types are ${ParamType.collect{it.desc}.join(', ').replaceAll(/(.*,)(.*)/, '$1 and $2')}.",
                   default: 'STRING'],
     description: [desc: "A description of the parameter.  This will be available to the user."],
     label:       [desc: "The label that will appear on the parameter form."],
     'default':   [desc: "The default value for the parameter."]]
  }

  /** The source code used to create this object */
  String source;

  /** The parameter name */
  String name;

  /** If this is non-null, inherit unspecified properties from this paramter;
   *  Also, call the super-param's validators before our own validation.
   */
  Param superParam;

  /** The type of the parameter */
  ParamType type;
  
  /** A one-line description of the parameter */
  String description;
  
  /** A label to be displayed when prompting for the parameter*/
  String label;

  /** A default value for the parameter */
  Object defaultValue;

  /** A list of values. */
  ListOfValues listOfValues;

  /** Perform whatever initialization is needed for this parameter. */
  Param init(HashMap args) {
    listOfValues?.init(args)
    return this
  }
  
  /** Return the values as a HashMap. */
  def export() {
    [name:         this.name,
     type:         this.type.desc,
     description:  this.description,
     label:        this.label,
     'default':    this.defaultValue
    ] + (
      (this.listOfValues)?[
        listOfValues: this.listOfValues.export()
      ] : [:]
    )
  }

  /** Explicit-argument constructor */
  public Param(String name, 
               ParamType type, 
               String description, 
               String label,
               Object defaultValue = null,
               Param superParam = null) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.label = label;
    this.defaultValue = (defaultValue instanceof String)?type.parse(defaultValue):defaultValue
    this.superParam = superParam;
  }

  /** Zero-argument constructor */
  public Param() {
  }

  boolean validate(def value) {
    return true
    // return type.convert(value);
  }

  /** Get a ParamValue of this parameter type */
  ParamValue getParamValue() {
    return new ParamValue(this)
  }

  /** Get a ParamValue of this parameter type 
   *  @value The value for the ParamValue.
   */
  ParamValue getParamValue(def value) {
    return new ParamValue(this, type.convert(value))
  }

}
