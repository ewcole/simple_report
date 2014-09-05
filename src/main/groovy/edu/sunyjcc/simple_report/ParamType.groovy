enum ParamType {
  string("STRING"), 
  // date("DATE"),
  // integer("INTEGER"),
  // number("NUMBER")

  ParamType(String desc) {this.desc = desc}
  private final String desc
  public String desc() {return desc}
}