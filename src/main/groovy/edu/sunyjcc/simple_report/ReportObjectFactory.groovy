package edu.sunyjcc.simple_report

/** This class creates and caches the objects representing parameters, 
 *  parameter forms, Jasper reports, and other things we will use the 
 *  system for.  It will be one of the primary classes for users of the
 *  library.
 */
public class ReportObjectFactory {
  /** The place we will get the source code for the objects we create */
    SourceFactory sourceFactory;

  /** A source for system parameters */
    ClientEnv clientEnv = new ClientEnv();

  /** Get the client environment; initialize if necessary. */
  ClientEnv getClientEnv() {
    if (!this.clientEnv) {
      this.clientEnv = new ClientEnv();
    }
    return this.clientEnv;
  }
  /** This will hold all of the report objects and provide functions to 
   *  get new ones.
   */
  def cache;

  /** Return a simple report builder */
  public SimpleReportBuilder getBuilder() {
    new SimpleReportBuilder(this)
  }

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
    def objTypes = "param param_form jrxml report sql job".split(/ +/);
    objTypes.inject([:]) {
      map, objType ->
        Closure getSrc = {
          String name ->
            def src = sf.getSource(objType, name);
        }
        Closure getObject = {
          name ->
            /** This turns the source code into report objects*/
            SimpleReportBuilder builder = new SimpleReportBuilder(this);
            builder.eval(getSrc(name))
        }
        if (objType == 'jrxml') {
          // Just return the raw source code
          getObject = { 
            name -> 
              def jasperSrc = getSrc(name)
              new JasperReportInstance(jasperSrc, this);
          }
        }
        if (objType == 'sql') {
          getObject = {
            name ->
              def queryText = getSrc(name)
              assert queryText?.size()
              // Extract the parameter names from this query
              def psql = SqlQueryEngine.parseSql(queryText)
              /** This turns the source code into report objects*/
              def builder = new SimpleReportBuilder(this);
              ParamForm superParamForm;
              try {
                superParamForm = this.getParamForm(name);
                println "superParamForm = $superParamForm"
              } catch (BuildException e) {
                // Cannot create the parameter form
                superParamForm = new ParamForm();
                println "param form $name not found."
              }
              superParamForm.reportObjectFactory = this;
              SimpleReport r = builder.report(name: name) {
                sql(query: queryText)
              }
              psql.paramRefs?.each {
                paramName ->
                if (!(superParamForm.params.containsKey(paramName))) {
                    Param superParam = null;
                    try {
                        superParam = this.getParam(paramName);
                        superParamForm.addParam(new Param(superParam));
                    } catch (BuildException e) {
                        // There is no pre-defined param,
                        superParamForm.addParam(builder.param(name: paramName));
                    }
                  }
              }
              r.params = superParamForm;
              assert r.getClass() == SimpleReport
              return r
          }
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
    def c = cache[normalizeTypeName(rawTypeName)];
    assert c
    return c
  }

  /** Clear the cache so that we can reload updated apps. */
  def clearCache() {
    this.cache = createCache(sourceFactory)
  }

  /** Set the source factory and create a new object cache.
   */
  public setSourceFactory(SourceFactory sourceFactory) {
    //println "in setSourceFactory($sourceFactory)"
    this.sourceFactory = sourceFactory
    //println "this.sourceFactory=${this.sourceFactory}"
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
  
  /** Get a report object 
   *  @param name The name of the report, without extension
   */
  public SimpleReport getReport(String name) {
    return getReportObject('report', name)
  }
  
  /** Get a Jasper Report object 
   *  @param name The name of the Jasper Report, without extension
   */
  public JasperReportInstance getJasperReport(String name) {
    return getReportObject('jrxml', name)
  }

  /** Get a SQL query object 
   *  @param name The name of the Sql Query, without extension
   */
  public SimpleReport getSql(String name) {
    getReportObject('sql', name)
  }

  /** Get a SimpleJob object
   *  @param name The name of the Job, without extension
   */
  public SimpleReport getJob(String name) {
    getReportObject('job', name)
  }

  /** Get the appropriate invocation object for this type and object name */
  public Invocation getInvocation(String objectType, String objectName) {
    def typeName = normalizeTypeName(objectType)
    
    return new Invocation(this, typeName, objectName)
  }

  /** Evaluate a string as a report object builder script. */
  public eval(String s) {
    /** This turns the source code into report objects*/
    SimpleReportBuilder builder = new SimpleReportBuilder(this);
    builder.eval(s)
  }

  /** Evaluate the closure into a report object */
  public build(Closure c) {
    c.delegate = new SimpleReportBuilder(this);
    c()
  }

  /** Public constructor with one SourceFactory argument 
   *  @param sourceFactory Get our source code from this SourceFactory.
   */
  public ReportObjectFactory(SourceFactory sourceFactory) {
    this.setSourceFactory(sourceFactory)
    assert this.cache
  }

  public ReportObjectFactory(SourceFactory sourceFactory,
                             ClientEnv     clientEnv) {
    this.setSourceFactory(sourceFactory)
    assert this.cache
    this.clientEnv = clientEnv;
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
