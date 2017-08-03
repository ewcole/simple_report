package edu.sunyjcc.simple_report;

import groovy.util.BuilderSupport
// See Groovy in Action, 8.6.1 Subclassing Builder Support, on p. 272.
// http://groovy.codehaus.org/api/groovy/util/BuilderSupport.html
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.6.8/groovy/util/NodeBuilder.java
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.8.0/groovy/xml/MarkupBuilder.java/
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.8.0/groovy/util/AntBuilder.java#AntBuilder.%3Cinit%3E%28%29

public class SimpleReportBuilder extends BuilderSupport {

  /** We will retrieve pre-built report objects from here when creating
   *  objects with inheritance.
   */
  ReportObjectFactory reportObjectFactory;

  private def reports = [:];

  private def debug(String text) {
    println text
  }

  private nodeFactory = [
    report: [
      create: {
        String name, Map attributes, def value ->
          def report = new SimpleReport(attributes)
          assert report
          if (value) {
            report.description = value
          }
          return report
      },
      implClass: SimpleReport,
    ],
    job: [
      create: {
        String name, Map attributes, def value ->
          SimpleJob job = new SimpleJob(attributes)
          assert job 
          return job
      },
      implClass: SimpleJob,
    ],
    jobEngine: [
      create: {
        String name, Map attributes, def value ->
          SimpleJobEngine engine = new SimpleJobEngine(attributes)
          assert engine
          return engine
      },
      implClass: SimpleJobEngine,
    ],
    params: [
      create: {
        String name, Map attributes, def value ->
          if (attributes.copyFrom?.size()) {
            debug("param(copyFrom: '${attributes.copyFrom}')");
            ParamForm p = reportObjectFactory.getParamForm(attributes.copyFrom);
            new ParamForm(p);
          } else {
            new ParamForm()
          }
      },
      implClass: ParamForm
    ],
    param: [
      create: {
        String name, Map attributes, def value ->
          debug ("in nodeFactory.param($name, $attributes, $value)") 
          assert name == 'param'
          assert attributes.name
          String paramTypeStr = (attributes.type?:'string').toLowerCase();
          ParamType paramType = ParamType[paramTypeStr];
          def param = new Param(attributes.name,
                                paramType,
                                attributes.description?:attributes.name,
                                attributes.label?:attributes.name,
                                attributes.default?:null)
          /* if (attributes.default) {
             param.default = attributes.default
             }*/
          if (value && !attributes?.desc) {
            attributes.desc = value.toString()
          }
          if (attributes?.value) {
            param.setValue(attributes.value)
          }
          return param;
      },
      implClass: Param,
    ],
    list_of_values: [
      create: {
        String name, Map attributes, def value ->
          def ks = attributes.keySet()
          if (!(ks.intersect(['sql', 'values'])?.size()) && value?.size()) {
            attributes += [sql: value]
          }
          return ListOfValues.build(attributes)
      },
      implClass: ListOfValues
    ],
    // lov: [
    //   create: {
    //     String name, Map attributes, def value ->
    //       def ks = attributes.keySet()
    //       if (!(ks.intersect(['query', 'values'])?.size()) && value.size()) {
    //         attributes += [sql: value]
    //       }
    //       return ListOfValues.build(attributes)
    //   },
    //   implClass: ListOfValues
    // ],
    csv: [
      create: {
        String name, Map attributes, def value ->
          debug ("in nodeFactory.csv($name, $attributes, $value)") 
          assert name == 'csv'
          def eng = new CsvQueryEngine(attributes)
          debug "nodeFactory.csv => $eng"
          assert eng
          return eng
      },
      implClass: CsvQueryEngine
    ],
    data_generator: [
      create: {
        String name, Map attributes, def value ->
          debug ("in nodeFactory.query_script($name, $attributes, $value)") 
          assert name == 'data_generator'
          def attrs = attributes?:[:];
          if (value && value instanceof Closure) {
            attrs.closure = value;
          }
          def eng = new ClosureQueryEngine(attrs)
          debug "nodeFactory.data_generator => $eng"
          assert eng
          return eng
      },
      implClass: ClosureQueryEngine
    ],
    sql: [
      create: {
        String name, Map attributes, def value ->
          debug ("in nodeFactory.sql($name, $attributes, $value)") 
          assert name == 'sql'
          def eng = new SqlQueryEngine(attributes)
          debug "nodeFactory.sql => $eng"
          assert eng
          return eng
      },
      implClass: SqlQueryEngine
    ],
    dynamic_sql: [
      create: {
        String name, Map attributes, def value ->
          debug ("in nodeFactory.dynamic_sql($name, $attributes, $value)") 
          assert name == 'dynamic_sql'
          def eng = new DynamicSqlQueryEngine(attributes)
          debug "nodeFactory.sql => $eng"
          assert eng
          return eng
      },
      implClass: DynamicSqlQueryEngine
    ],
  ];

  /** A map from report object class name to the method used to build it. */
  def classMethodNames = nodeFactory.collect {[it.key, it.value.implClass]}.reverse().inject([:]) {
    m, v ->
      m << [(v[1]): v[0]]
  }

  /** Each entry in this table is a closure that attaches the child to the 
   *  parent.  The keys for the map are a subset of the cross-product of the 
   *  classes that can go into a SimpleReport.
   *
   *  Note that the keys of the map are actual Java classes, not their names.
   *  It is very important that they be wrapped in parentheses.
   */
  def addChildFarm = [
    (SimpleReport): [
      (Param): {
        parent, child ->
          parent.addParam(child)
      },
      (ParamForm): {
        parent, child ->
          assert !parent.params
          parent.params = child
      },
      (SqlQueryEngine): {
        parent, child ->
          parent.queryEngine = child
      },
      (DynamicSqlQueryEngine): {
        parent, child ->
          parent.queryEngine = child
      },
      (ClosureQueryEngine): {
        parent, child ->
          parent.queryEngine = child
      },
      (CsvQueryEngine): {
        parent, child ->
          parent.queryEngine = child
      },
    ],
    (SimpleJob): [
      (SimpleJobEngine): {
        parent, child ->
          debug("Adding $child to $parent")
          parent.jobEngine = child.closure
          debug("After adding $child to $parent")
          // It's important not to return the closure;
          //    otherwise it will be executed right away
          //    instead of the time that it is appropriate.
          return null;
      },
      (Param): {
        parent, child ->
          parent.addParam(child)
      },
      (ParamForm): {
        parent, child ->
          assert !parent.params
          parent.params = child
      },
    ],
    (ParamForm):[
      (Param): {
        parent, child ->
          parent.addParam(child);
      },
    ],
    (Param): [
      (ListOfValues): {
        parent, child ->
          parent.listOfValues/*[child.name]*/ = child
      }
    ],
  ]

  Object createNode(Object name) {
    createNode(name, [:], null)
  }
  Object createNode(Object name, Object value) {
    createNode(name, [:], value)
  }

  Object createNode(Object name, Map attributes) {
    createNode(name, attributes, null)
  }

  Object createNode(Object name, Map attributes, Object value) {
    debug "createNode($name)"
    if (!nodeFactory[name]) {
      throw new BuildException("'$name' is not a valid build method.  Valid values are ["
                               + nodeFactory.keySet().join(', ') + "]");
    }
    assert name in nodeFactory.keySet()
    debug "After assert; call nodeFactory[$name]($name, $attributes, $value)}"
    def n = nodeFactory[name].create(name, attributes, value)
    debug "createNode => $n"
    return n
  }

  def getBuildDocs() {
    nodeFactory.keySet().inject([:]) {
      objMap, keyword ->
        def z = nodeFactory[keyword]
        def childClasses = ((addChildFarm[z.implClass])?.keySet())?:[]
        def childMethods = nodeFactory.grep {childClasses.contains(it.value.implClass)}
                               .collect {it.key}
        objMap << ["$keyword": [methodName: "$keyword",
                                implClass: z.implClass,
                                childMethods: childMethods]]
        objMap
    }
  }

  void setParent(Object parent, Object child) {
    debug "in setParent(${parent.getClass()}, ${child.getClass()})"
    debug "parent=$parent"
    //debug "parent.addChild = ${parent?.addChild}"
    //debug "parent.addChild.keySet()=${parent.addChild?.keySet()}"
    //assert  parent.addChild.keySet().contains(child.name)
    def parentClass = parent.getClass()
    def farm = addChildFarm[parent.getClass()]
    debug "farm=$farm"
    if (! farm[child.getClass()]) {
      def parentMethod = classMethodNames[parentClass]
      def childMethod = classMethodNames[child.getClass()]
      throw new BuildException("You cannot embed a $childMethod within a $parentMethod. "
               + "Valid options are: [${farm.keySet().collect { classMethodNames[it]}.join(',')}]")
    }
    def z = farm[child.getClass()](parent, child)
    debug "z=$z"
    z
  }

  void nodeCompleted(Object parent, Object node) {}
  
  /** Zero-argument constructor */
  SimpleReportBuilder() {
    super()
  }

  /** Create this with a reference to a ReportObjectFactory; this will
   *  be used for implementing inheritance 
   */
  SimpleReportBuilder(ReportObjectFactory reportObjectFactory) {
    super()
    this.reportObjectFactory = reportObjectFactory;
  }

  /** Evaluate a report builder script and return the results */
  def eval(String text) {
    def shell = new GroovyShell()
    // wrap the script in a closure before evaluating.
    try {
      // Wrap the text in a closure so that it doesn't execute
      //   immediately.  This gives us the chance to change
      //   its delegate.
      Closure c = shell.evaluate("{->$text}")
      c.setDelegate(this)
      // Execute the build script and add the
      // source code to the object created.
      def b = c()
      if (b instanceof Buildable) {
        b.source = text
      }
      return b;
    } catch (BuildException e) {
      e.source = text;
      throw e
    }
  }
 
}
