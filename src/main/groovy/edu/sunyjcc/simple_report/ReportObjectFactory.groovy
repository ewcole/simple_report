package edu.sunyjcc.simple_report

/** This class creates and caches the objects representing parameters, 
 *  parameter forms, Jasper reports, and other things we will use the 
 *  system for.  It will be one of the primary classes for users of the
 *  library.
 */
public class ReportObjectFactory {
  /** The place we will get the source code for the objects we create */
  SourceFactory sourceFactory;

  /** This turns the source code into report objects*/
  SimpleReportBuilder builder = new SimpleReportBuilder();

  /** This will hold all of the report objects and provide functions to 
   *  get new ones.
   */
  def cache;

  /** 
   *  Filter out non-standard type names
   *  @param typeName Un-normalized type name
   */
  static String normalizeTypeName(String typeName) {
    return SourceFactory.normalizeTypeName(typeName);
  }

  /* Return a cache object that gets its source from the sf SourceFactory
   * @param sf It will get the source for new objects from this factory.
   * @param 
   */
  LinkedHashMap createCache(SourceFactory sf) { 
    assert sf
    def objTypes = "param param_form jrxml".split(/ +/);
    objTypes.inject([:]) {
      map, objType ->
        Closure getSrc = {
          String name ->
            def src = sf.getSource(objType, name);
        }
        Closure getObject = {
          name ->
            builder.eval(getSrc(name))
        }
        if (objType == 'jrxml') {
          getObject = { name -> getSrc(name)}
        }
        map[objType] = [
          // Get the object's source code.
          getSrc:    getSrc.memoize(),
          // Get the compiled object 
          getObject: getObject.memoize()
        ]
        return map;
    }
  }

  /** Get the appropriate type cache for the input object. */
  def getTypeCache(String rawTypeName) {
    assert cache
    cache[normalizeTypeName(rawTypeName)];
  }

  /** Set the source factory and create a new object cache.
   */
  public setSourceFactory(SourceFactory sourceFactory) {
    println "in setSourceFactory($sourceFactory)"
    this.sourceFactory = sourceFactory
    println "this.sourceFactory=${this.sourceFactory}"
    this.cache = createCache(sourceFactory)
    assert this.cache
  }

  /** Create a requested object if not already in the cache. */
  def getReportObject(String rawTypeName, String objName) {
    assert cache
    // Note: you can't say .getObject() because that closure 
    //       is memoized.  
    getTypeCache(rawTypeName).getObject.call(objName);
  }


  /** Get a parameter object 
   *  @param name The name of the parameter, without extension
   */
  public Param getParam(String name) {
    return getReportObject('param', name)
  }

  /** Get a parameter form object 
   *  @param name The name of the parameter form, without extension
   */
  public ParamForm getParamForm(String name) {
    return getReportObject('param_form', name)
  }
  
  /** Get a Jasper Report object 
   *  @param name The name of the Jasper Report, without extension
   */
  public String getJrxml(String name) {
    return getReportObject('param_form', name)
  }

  /** Public constructor with one SourceFactory argument 
   *  @param sourceFactory Get our source code from this SourceFactory.
   */
  public ReportObjectFactory(SourceFactory sourceFactory) {
    this.setSourceFactory(sourceFactory)
    assert this.cache
  }

  /** Public constructor with generic object argument */
  // public ReportObjectFactory(Object o) {
  //   println "Object o = $o"
  //   if (o instanceof Map) {
  //     println "o instanceof Map"
  //     if (o.sourceFactory) {
  //       this.setSourceFactory((SourceFactory)o.sourceFactory);
  //     }
  //   } else if (o instanceof SourceFactory) {
  //     println "o instanceof SourceFactory"
  //     this.setSourceFactory((SourceFactory)o.sourceFactory);
  //   } else {
  //     setSourcefactory((SourceFactory)o);
  //   }
  //   assert this.cache
  // }
  
}