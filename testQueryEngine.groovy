package edu.sunyjcc.simple_report

import edu.sunyjcc.util.JccConnectionManager

assert args.size()
def cm = new JccConnectionManager(args[0])
assert cm.ora

def q = new SqlQueryEngine(query: "select stvterm_code, stvterm_desc from stvterm t where t.stvterm_code like '2010%'")
.init(sql: cm.ora);
assert q
def e = q.execute()
println "e=$e"
println "e.columns == ${e.columns}"
//println "e.rows = (${e.rows.size()}) ${e.rows} "
e.rows.each {
  row ->
    println row
}

println "**********************************************************************"
println "* Test the query with parameters."
println "**********************************************************************"
q = new SqlQueryEngine(query: "select stvterm_code, stvterm_desc from stvterm t where t.stvterm_code = :term_code")
.init(sql: cm.ora);
def params = new SimpleReportBuilder().params {
  param(name: 'term_code', value: '201312')
}
params.setValues([term_code: '201312'])
println "params=${params.export()}"
e = q.execute(params)
println e.rows