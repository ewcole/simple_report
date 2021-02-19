package edu.sunyjcc.simple_report

public class SystemParamsTest extends GroovyTestCase {
  
  void printBanner(String s) {
    println ""
    println ""
    println ("*" * 79)
    println "******** $s ********************"
    println ("*" * 79)
  }

  void testAddSystemParam() {
    printBanner("testAddSystemParam");
    def s = new SystemParams();
    Integer i = 0;
    Integer j = 1;
    s.addSystemParam('next', {-> ++i});
    assert s.metaClass.properties
    assert s.next == 1;
    assert s.next == 2;
    s.addSystemParam('n2') {-> ++j};
    assert s.n2 == 2;
    // Test value parameters
    print "Test value parameters"
    s.addSystemParam('StringValue', "This is a String");
    assert s.StringValue == "This is a String";
    s.addSystemParam('IntValue', 42);
    assert s.IntValue == 42;
    def x = 35
    s.addSystemParam('xVal', x);
    assert s.xVal == 35;
    x = 472;
    assert s.xVal == 35;
  }
  

  void testList() {
    printBanner("testList");
    def s = new SystemParams();
    s.addSystemParam("quarter") {->1/4};
    s.addSystemParam("half", 0.5);
    s.addSystemParam("stringy", "String");
    def props = s.list();
    assert props;
    props.each {
      println ">> ${it.key} = ${it.value} = ${it.value.value}"
    }
    assert props.quarter?.value == 1/4;
    assert props.half?.value ==  0.5
    assert props.stringy?.value == "String"
  }

  void testMapConstructor() {
    printBanner "testMapConstructor"
    def sysP = new SystemParams(username:   "fred01",
                                userId: "10101",
                                four:   {-> 2 + 2});
    assert sysP;
    println "map = ${sysP.list()}";
    assert sysP.username == "fred01"
    assert sysP.userId == "10101"
    assert sysP.four == 4;
    assert sysP.size() == 3;
    sysP.each {
      println it
    }
  }

  void testEach() {
    printBanner "testEach"
    def sysP = new SystemParams(username:   "fred01",
                                userId: "10101",
                                four:   {-> 2 + 2});
    assert sysP;
    println "map = ${sysP.list()}";
    sysP.list().each {
      println "${it.key}->${it.value.value}"
      assert it.value instanceof SystemParam
    }

  }
}
