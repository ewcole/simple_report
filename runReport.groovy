@GrabResolver(name="jccprod", root="file://x:/source/mvn/prod/")
@Grab(group="edu.sunyjcc", module="simple_report", version="0.0.1")

package edu.sunyjcc.simple_report

import edu.sunyjcc.util.*
//import edu.sunyjcc.simple_report.*

def cli = new CliBuilder(usage: "Run a special report from the command line")
cli.u(longOpt: "userid", args: 1, required: true, "Oracle userid (username/password@db")
cli.r(longOpt: "report", args: 1, required: true, "The name of the report file")

// Make sure the parameters are there
def opt = cli.parse(args)
if (!opt) {
  return -1
}

def repScript = new File(opt.r)
assert repScript.exists()

def optParams = opt.arguments().inject([:]) {
  map, value ->
    (value =~ /^(.*?)=(.*)/).each {
      m ->
        map[m[1] as String] = m[2] as String
    }
    return map
}
println "optParams = $optParams"
def sql = new JccConnectionManager(opt.u).ora
if (!sql) {
  cli.usage()
  println ""
  println "Invalid database credentials!"
  return
}

//////////////////////////////////////////////////////////////////////////
// Run the report
//////////////////////////////////////////////////////////////////////////
def b = new SimpleReportBuilder()
def r = b.eval(repScript.text).init(sql: sql)
assert r instanceof SimpleReport
def p = r.execute(optParams)

//////////////////////////////////////////////////////////////////////////
// Print out the results
//////////////////////////////////////////////////////////////////////////
def rpad = {
  String text, size ->
    def colSize = size - text.size()
    text + (" " * ((colSize>0)?colSize:0))
}

def colHeaders = p.columns.inject([]) {
  h, col ->
    h << rpad(col.name, col.displaySize)
    h
}
println ""
println colHeaders.join(" ")
println colHeaders.collect { "-" * it.size() }.join(" ")
p.rows.each {
  row ->
    println p.columns.collect {
      col ->
        def colSize = (col.name.size() > col.displaySize)?col.name.size():col.displaySize
        rpad(row[col.name] as String, colSize)
    }.join(" ")
}

println "${p.rows.size()} rows selected."
