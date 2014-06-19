<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#sec-1">Synopsis</a></li>
<li><a href="#sec-2">Sample application</a></li>
</ul>
</div>
</div>


# Synopsis

I am suggesting a solution to several problems we are facing with deploying our new reports.  Here are some issues that should be resolved by the service.
-   Defining and collecting parameters for a Jasper Report
-   Defining and collecting parameters for a CSV export file
-   Re-using parameter definitions between reports
-   Simple creation and deployment of CSV generators

The reports will be saved as Groovy scripts that are evaluated by a builder.  

# Sample application

Suppose you have a report file, [sampleReport.srep](sampleReport.srep).  

    1  report(name: 'Term Report') {
    2    param(name: 'app_type_code', desc: "Application type code", default: 'FORM') 
    3    sql(query: """
    4      select app_sa, app_title
    5          from apps a 
    6          where a.app_type_code = :app_type_code
    7          order by app_sa""")
    8  }

You can execute it with a simple command line script.

    runReport -u %userid% -r sampleReport.srep app_type_code=GRLS

    APP_SA                 APP_TITLE
    ---------------------- -------------------------------
    804                    Add-On Menu Administration
    807                    Add-On Menu Administration
    823                    Popsel Reference Editor
    824                    Key Inventory
    4 rows selected.

The [runReport.groovy](runReport.groovy) script is just a shell; it has 27 lines that gather the  report parameters and connect to the database, 22 lines that format the report for output, and only 4 lines that actually run the report (counting an assertion).

    1  def b = new SimpleReportBuilder()
    2  def r = b.eval(repScript.text).init(sql: sql)
    3  assert r instanceof SimpleReport
    4  def p = r.execute(optParams)

## Downloading the application

My demonstration is in the java/lib/simple\_report repository on the [GitBlit server](https://inb01.sunyjcc.edu/git/).  You can go directly to the summary page here.

<https://inb01.sunyjcc.edu/git/summary/java!lib!simple_report.git>

It's probably better to take the url from the **repository url** listed on this page, but you can clone it with this command (the difference is that GitBlit puts your user name in its link.

    git clone https://inb01.sunyjcc.edu/git/git/java/lib/simple_report.git

To build the project, you need to have [Gradle](http://www.gradle.org/) installed on your system.  Run `build.cmd` and it should create the jar files.
