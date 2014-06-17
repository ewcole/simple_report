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

def sql = new JccConnectionManager(opt.u).ora
if (!sql) {
  cli.usage()
  println ""
  println "Invalid database credentials!"
  return
}
def b = new SimpleReportBuilder()
def r = b.eval(repScript.text).init(sql: sql)
assert r instanceof SimpleReport
//println "r =~ ${r.export()}"
def p = r.execute(term_code: '201305')

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
        rpad(row[col.name], colSize)
    }.join(" ")
}

println "${p.rows.size()} rows selected."
