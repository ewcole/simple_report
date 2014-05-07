package edu.sunyjcc.simple_report;

import groovy.util.BuilderSupport
// See Groovy in Action, 8.6.1 Subclassing Builder Support, on p. 272.
// http://groovy.codehaus.org/api/groovy/util/BuilderSupport.html
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.6.8/groovy/util/NodeBuilder.java
// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.8.0/groovy/xml/MarkupBuilder.java/

// http://grepcode.com/file/repo1.maven.org/maven2/org.codehaus.groovy/groovy/1.8.0/groovy/util/AntBuilder.java#AntBuilder.%3Cinit%3E%28%29

public class SimpleReportBuilder extends BuilderSupport {

  private def reports = [:];

  private reportSchema = [
    report: [
      createNode: {
        String name, Map attributes, def value ->
          def report = [name: name,
                        report: new SimpleReport(attributes),
                        details: []
                       ]
          assert name == 'report'
          if (value) {
            report.details.add(value)
          }
          report.addChild = [
            param: {
              parent, child ->
                println "(param)child=$child"
                parent.details.add(child)
            }
          ];
          return report
      }
    ],
    param: [      
      createNode: {
        String name, Map attributes, def value ->
          println ("in param.createNode($name, $attributes, $value)") 
          assert name == 'param'
          assert attributes.name
          def param = [name: name,
                       param_name: attributes.name,
                       type: attributes.type?:String,
                       details: []]
          if (attributes.default) {
            param.default = attributes.default
          }
          if (value) {
            attributes.desc = value.toString()
          }
          param.addChild = [
            doc: {
              parent, child ->
                println "In param.addChild()"
                parent.params.add(child)
            }
          ];
          return param;
      }],
    doc: [],
  ];




  Object createNode(Object name) {
    println "createNode($name)"
    if (!reportSchema[name]) {
      println "Invalid object: $name"
    }
    assert name in reportSchema.keySet()
    println "After assert; reportSchema[$name]=${reportSchema[name]}"
    reportSchema[name].createNode(name, [:], null)
  }
  Object createNode(Object name, Object value) {
    println "createNode($name)"
    if (!reportSchema[name]) {
      println "Invalid object: $name"
    }
    assert name in reportSchema.keySet()
    println "After assert; reportSchema[$name]=${reportSchema[name]}"
    reportSchema[name].createNode(name, [:], value)
  }
  Object createNode(Object name, Map attributes) {
    println "createNode($name)"
    if (!reportSchema[name]) {
      println "Invalid object: $name"
    }
    assert name in reportSchema.keySet()
    println "After assert; reportSchema[$name]=${reportSchema[name]}"
    reportSchema[name].createNode(name, attributes, null)
  }
  Object createNode(Object name, Map attributes, Object value) {
    println "createNode($name)"
    if (!reportSchema[name]) {
      println "Invalid object: $name"
    }
    assert name in reportSchema.keySet()
    println "After assert; reportSchema[$name]=${reportSchema[name]}"
    reportSchema[name].createNode(name, attributes, value)
  }

  void setParent(Object parent, Object child) {
    println "in setParent(${parent.name}, ${child.name})"
    println "parent=$parent"
    println "parent.addChild = ${parent.addChild}"
    println "parent.addChild.keySet()=${parent.addChild.keySet()}"
    assert  parent.addChild.keySet().contains(child.name)
    parent.addChild[child.name](parent, child)
  }

  void nodeCompleted(Object parent, Object node) {}
  
  SimpleReportBuilder() {
    super()
  }
  
  public static void main(String[] args) {
    def a = new SimpleReportBuilder()
    def r = a.report(name: "ghostHunt") {
      param(name: 'scoobydoo', default: 'scared')
    }
    println r
    def j = new groovy.json.JsonOutput()
    //println j.prettyPrint(j.toJson(r))
  }

}