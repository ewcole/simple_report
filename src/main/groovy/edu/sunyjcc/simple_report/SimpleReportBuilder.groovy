package edu.sunyjcc.simple_report;

import groovy.util.BuilderSupport
// See Groovy in Action, 8.6.1 Subclassing Builder Support, on p. 272.
// http://groovy.codehaus.org/api/groovy/util/BuilderSupport.html
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.6.8/groovy/util/NodeBuilder.java
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.8.0/groovy/xml/MarkupBuilder.java/

// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.8.0/groovy/util/AntBuilder.java#AntBuilder.%3Cinit%3E%28%29

public class SimpleReportBuilder extends BuilderSupport {

  private def reports = [:];

  private nodeFactory = [
    report: {
      String name, Map attributes, def value ->
        def report = new SimpleReport(attributes)
        assert report
        if (value) {
          report.description = value
        }
        return report
    },
    param: {
      String name, Map attributes, def value ->
        println ("in nodeFactory.param($name, $attributes, $value)") 
        assert name == 'param'
        assert attributes.name
        def param = new Param(attributes.name,
                              attributes.type?:String,
                              attributes.description?:attributes.name,
                              attributes.label?:attributes.name)
        /* if (attributes.default) {
           param.default = attributes.default
           }*/
        if (value && !attributes?.desc) {
          attributes.desc = value.toString()
        }
        return param;
    },
  ];

  def addChildFarm = [
    (SimpleReport): [
      (Param): {
        parent, child ->
          parent.addParam(child)
      }
    ]
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
    println "createNode($name)"
    if (!nodeFactory[name]) {
      println "Invalid object: $name"
    }
    assert name in nodeFactory.keySet()
    println "After assert; call nodeFactory[$name]($name, $attributes, $value)}"
    nodeFactory[name](name, attributes, value)
  }

  void setParent(Object parent, Object child) {
    println "in setParent(${parent.name}, ${child.name})"
    println "parent=$parent"
    //println "parent.addChild = ${parent.addChild}"
    //println "parent.addChild.keySet()=${parent.addChild.keySet()}"
    //assert  parent.addChild.keySet().contains(child.name)
    addChildFarm[parent.getClass()][child.getClass()](parent, child)
  }

  void nodeCompleted(Object parent, Object node) {}
  
  SimpleReportBuilder() {
    super()
  }
  
}