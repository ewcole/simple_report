package edu.sunyjcc.simple_report

/** Objects that implement this interface will have methods that return the 
 *  source code for various objects defined in the simple_reports library.
 *  SourceFactory objects are not responsible for caching; that will be done 
 *  elsewhere.
 */
public abstract class SourceFactory {

  /** Rules to do non-trivial translations
   */
  static final def typeNameTranslationRules = [
    jasper: "jrxml"
  ];

  /** Translate the type string into the official version
   *  @param  typeName The name of the type you are searching for.
   */
  static String normalizeTypeName(String typeName) {
    def name = typeName.replaceAll(~/([a-z])([A-Z])/) {
      "${it[1]}_${it[2]}".toLowerCase()
    }.toLowerCase()
    if (typeNameTranslationRules[name]) {
      return typeNameTranslationRules[name]
    } else {
      return name
    }
  }

  /** Go to the source
   *  @param objType The kind of object that we are trying to get
   *  @param objName The name of the object.
   */
  abstract String getSourceText(String objType, String objName);

  /** Get the source for a parameter object 
   *  @param name The name of the parameter you want to fetch
   */
  String getParamSource(String name) {
    def pf = getSourceText("param", "${name}.groovy")
  }

  /** Get the source for a parameter form object 
   *  @param name The name of the parameter form you want to fetch
   */
  public String getParamFormSource(String name) {
    def pf = getSourceText("param_form", "${name}.groovy")
  }

  /** Get a .jrxml file, defining a Jasper Report. 
   *  @param name The name of the Jasper Report you want to fetch
   */
  public String getJrxmlSource(String name) {
    def pf = getSourceText("jrxml", "${name}.jrxml")
  }
  
  /** Get a .groovy file, defining a Simple Report. 
   *  @param name The name of the report you want to fetch
   */
  public String getReportSource(String name) {
    def pf = getSourceText("report", "${name}.groovy")
  }
  
  /** Get the source for an object of the desired type 
   *  @param type   The kind of object; one of 'param', 'paramForm', 'jrxml'.
   */
  public String getSource(String type, String name) {
    def nType = normalizeTypeName(type)
    def ext = (nType == 'jrxml')?'jrxml':'groovy';
    def qName = "${name}.${ext}"
    getSourceText(nType, qName)
  }

  /** List the objects this factory can find the source for */
  public abstract ArrayList list();

}
