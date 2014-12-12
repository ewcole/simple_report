report (name: 'simple_report_types') {
  def reportObjectTypes = [
    [name: 'param', desc: 'A single parameter'],
    [name: 'param_form', desc: 'A parameter form'],
    [name: 'report', desc: 'A groovy-based report'],
    [name: 'jrxml', desc: 'A Jasper Report'],
  ]
  def s = new StringWriter()
  s.println "Name,Description"
  reportObjectTypes.each {
    s.println "$it.name,$it.desc"
  }
  csv(text: s.toString())
}