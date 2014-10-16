package edu.sunyjcc.simple_report

/** 
 */
public class ReportObjectFactory {
  /** The place we will get the source code for the objects we create */
  SourceFactory sourceFactory

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
   * @param It will get the source for new objects from this factory.
   */
  static def createCache(SourceFactory sf) { 
    assert sf
    def objTypes = "param param_form jrxml".split(/ +/);
    objTypes.inject([:]) {
      map, objType ->
        map[objType] = [
          // Get the object
          getSrc: {
            String name ->
              def src = sf.getSource(objType, name);
          }
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

  public Param     getParam(String name) {}
  public ParamForm getParamForm(String name) {}
  public Object    getJrxml(String name) {}

  /** Public constructor with one SourceFactory argument */
  public ReportObjectFactory(SourceFactory sourceFactory) {
    this.setSourceFactory(sourceFactory)
    assert this.cache
  }

  /** Public constructor with generic object argument */
  public ReportObjectFactory(Object o) {
    println "Object o = $o"
    if (o instanceof Map) {
      println "o instanceof Map"
      if (o.sourceFactory) {
        this.setSourceFactory((SourceFactory)o.sourceFactory);
      }
    } else if (o instanceof SourceFactory) {
      println "o instanceof SourceFactory"
      this.setSourceFactory((SourceFactory)o.sourceFactory);
    } else {
      setSourcefactory((SourceFactory)o);
    }
    assert this.cache
  }
  
}