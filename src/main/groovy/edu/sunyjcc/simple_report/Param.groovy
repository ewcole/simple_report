package edu.sunyjcc.simple_report;

/** A parameter definition to be used in a report or application invocation.
 */
public class Param implements Exportable, Buildable {

  /** The factory that created this object */
  ReportObjectFactory reportObjectFactory;
  
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
     copyFrom:    [desc: """The name of another parameter, which will serve as the prototype for
                            this one.  Any properties that are not set on the new parameter will 
                            be inherited from the prototype.  Do not include 'param/' at the 
                            beginning of the name."""], 
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
  /** Get the name */
  String getName() {
    name?:(superParam?.name);
  }
  
  /** If this is non-null, inherit unspecified properties from this paramter;
   *  Also, call the super-param's validators before our own validation.
   */
  Param superParam;

  
  /** The type of the parameter */
  ParamType type; //  = ParamType.string;
  ParamType getType() {
    (type?:superParam?.type)?:ParamType.string;
  }
  
  /** A one-line description of the parameter */
  String description;
  String getDescription() {
    description?:(superParam?.description);
  }

  /** Return a plausible default label for the parameter */
  private static String defaultLabel(Param p) {
    return p.name?.split(/_/).collect {it.capitalize()}.join(' ');
  }
  
  /** A label to be displayed when prompting for the parameter*/
  private String label;
  String getLabel() {
    // The logic has been complicated by inheritance.  We return the label if
    //  there is one
    if (this.label?.size()) {
      // 1. The label is explicitly defined, so return it.
      return this.label
    } else if (superParam?.label?.size()) {
      // 2. There is a label in the super parameter.  Check it out.
      def superLabel = superParam.label;
      if (superLabel == defaultLabel(superParam)) {
        // 2a. Don't return the superParameter's default label, create our own. 
        return defaultLabel(this);
      } else {
        // 2b. The super label is explicitly defined, so return it.
        return superLabel;
      }
    } else {
      // 3. Create a default label.
      return defaultLabel(this);
    }
  }

  /** A default value for the parameter */
  Object defaultValue;
  Object getDefaultValue() {
    defaultValue?:(superParam?.defaultValue);
  }

  /** A list of values. */
  ListOfValues listOfValues;
  ListOfValues getListOfValues() {
    listOfValues?:(superParam?.listOfValues);
  }

  /** Perform whatever initialization is needed for this parameter. */
  Param init(HashMap args) {
    getListOfValues()?.init(args)
    return this
  }
  
  /** Return the values as a HashMap. */
  def export() {
    [name:         this.getName(),
     type:         this.getType()?.desc,
     description:  this.getDescription(),
     label:        this.getLabel(),
     'default':    this.getDefaultValue()
    ] + (
      (this.getListOfValues())?[
        listOfValues: this.getListOfValues().export()
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
    this.type = type?:ParamType.string;
    this.description = description;
    this.label = label;
    this.defaultValue = (defaultValue instanceof String)?type.parse(defaultValue):defaultValue
    this.superParam = superParam;
  }

  /** Zero-argument constructor */
  public Param() {
  }

  public Param(Param p) {
    setSuperParam(p);
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
