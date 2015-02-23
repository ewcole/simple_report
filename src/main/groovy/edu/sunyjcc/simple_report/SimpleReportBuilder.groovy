package edu.sunyjcc.simple_report;

import groovy.util.BuilderSupport
// See Groovy in Action, 8.6.1 Subclassing Builder Support, on p. 272.
// http://groovy.codehaus.org/api/groovy/util/BuilderSupport.html
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.6.8/groovy/util/NodeBuilder.java
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.8.0/groovy/xml/MarkupBuilder.java/
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.8.0/groovy/util/AntBuilder.java#AntBuilder.%3Cinit%3E%28%29

public class SimpleReportBuilder extends BuilderSupport {

  private def reports = [:];

  private def debug(String text) {
    // println text
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
    params: [
      create: {
        String name, Map attributes, def value ->
          new ParamForm()
      },
      implClass: ParamForm
    ],
    param: [
      create: {
        String name, Map attributes, def value ->
          debug ("in nodeFactory.param($name, $attributes, $value)") 
          assert name == 'param'
          assert attributes.name
          def param = new Param(attributes.name,
                                attributes.type?:ParamType.string,
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
          if (!(ks.intersect(['sql', 'values']).size()) && value.size()) {
            attributes += [sql: value]
          }
          return ListOfValues.build(attributes)
      },
      implClass: ListOfValues
    ],
    lov: [
      create: {
        String name, Map attributes, def value ->
          def ks = attributes.keySet()
          if (!(ks.intersect(['query', 'values'])?.size()) && value.size()) {
            attributes += [sql: value]
          }
          return ListOfValues.build(attributes)
      },
      implClass: ListOfValues
    ],
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
  ];

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
      (CsvQueryEngine): {
        parent, child ->
          parent.queryEngine = child
      }
    ],
    (ParamForm):[
      (Param): {
        parent, child ->
          parent.params[child.name] = child
      },
    ],
    (Param): [
      (ListOfValues): {
        parent, child ->
          parent.listOfValues[child.name] = child
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
      debug "Invalid object: $name"
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
    //debug "parent.addChild = ${parent.addChild}"
    //debug "parent.addChild.keySet()=${parent.addChild.keySet()}"
    //assert  parent.addChild.keySet().contains(child.name)
    def z = addChildFarm[parent.getClass()][child.getClass()](parent, child)
    debug "z=$z"
    z
  }

  void nodeCompleted(Object parent, Object node) {}
  
  SimpleReportBuilder() {
    super()
  }

  /** Evaluate a string and return the results */
  def eval(String text) {
    def shell = new GroovyShell()
    // wrap the script in a closure before evaluating.
    Closure c = shell.evaluate("{->$text}")
    c.setDelegate(this)
    def b = c()
    if (b instanceof Buildable) {
      b.source = text
    }
    return b;
  }
 
}