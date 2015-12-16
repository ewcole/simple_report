package edu.sunyjcc.simple_report


/** Test the ParamType enum. */
public class ParamTypeTest extends GroovyTestCase {

  def banner(String text) {
    "********** ${text} **********"
  }

  void testNumberType() {
    assert ParamType.number.parse(12) == 12;
    assert ParamType.number.parse("12") == 12;
    assert ParamType.number.parse("12.3") == 12.3;
    assert ParamType.number.parse(12.3) == 12.3;
  }
}