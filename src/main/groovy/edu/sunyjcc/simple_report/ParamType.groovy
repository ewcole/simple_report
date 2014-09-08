enum ParamType {
  string("STRING", {it.toString()}), 
  // date("DATE"),
  // integer("INTEGER"),
  // number("NUMBER")

  ParamType(String desc, Closure convert) {
    this.desc = desc
    this.convert = convert
  }

  private final String desc
  Closure convert;
  public String desc() {return desc}
}