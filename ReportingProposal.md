<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#sec-1">Synopsis</a></li>
<li><a href="#sec-2">Some Examples of the DSL</a></li>
<li><a href="#sec-3">A Sample Application</a></li>
<li><a href="#sec-4">Architecture</a></li>
<li><a href="#sec-5">Appendices</a></li>
</ul>
</div>
</div>


# Synopsis

I am suggesting a solution to several problems we are facing with deploying our new reports.  Here are some issues that should be resolved by the service.
-   Defining and collecting parameters for a Jasper Report
-   Defining and collecting parameters for a CSV export file
-   Re-using parameter definitions between reports
-   Simple creation and deployment of CSV generators

The source code of the reports, parameters, parameter lists, and other objects is completely independent of the environment in which it will be executed.  This means that the same code can be used in a Job Sumbission script or a Grails application, or anything in between.

It is also a text-based format, so it will be easy to use version control for them.  The parameter, parameter list, and report definitions would be stored in the file system.  It should be relatively simple to implement an inheritance scheme.

# Some Examples of the DSL

Here are some examples from the unit tests that show how you might use the domain specific language.

## Creating a Parameter

    1  def p = a.param(name: 'scoobydoo', default: 'scared',
    2                  value: 'hungry');

(From [SimpleReportBuilderTest.groovy](src\test\groovy\edu\sunyjcc\simple_report\SimpleReportBuilderTest.groovy))

## Creating a Parameter List

    1  def p = a.params {
    2    param(name: 'scoobydoo', default: 'scared')
    3    param(name: 'shaggy', label: "Shaggy", default: "hungry")
    4  }

(From [SimpleReportBuilderTest.groovy](src\test\groovy\edu\sunyjcc\simple_report\SimpleReportBuilderTest.groovy))

## Creating a Report with a CSV Data Source

    1  def e = a.report() {
    2    csv(text: "A, B, C\n1,2,3"){}
    3  }

(From [SimpleReportBuilderCsvTest.groovy](src\test\groovy\edu\sunyjcc\simple_report\SimpleReportBuilderCsvTest.groovy))

# A Sample Application

Suppose that you wanted to have a report of all Grails applications in our system.  You could write a report definition file, [sampleReport.srep](sampleReport.srep).  

    report(name: 'Term Report') {
      param(name: 'app_type_code', desc: "Application type code", default: 'FORM') 
      sql(query: """
        select app_sa, app_title
            from apps a 
            where a.app_type_code = :app_type_code
            order by app_sa""")
    }

Run the report by invoking it from a Groovy script that I wrote.

    runReport.groovy -u %userid% -r sampleReport.srep app_type_code=GRLS

With the following output.

    APP_SA                 APP_TITLE
    ---------------------- -------------------------------
    804                    Add-On Menu Administration
    807                    Add-On Menu Administration
    823                    Popsel Reference Editor
    824                    Key Inventory
    4 rows selected.

The [runReport.groovy](runReport.groovy) script is just a shell; it has 27 lines that gather the  report parameters and connect to the database, 22 lines that format the report for output, and only 4 lines that actually run the report (counting an assertion that guarantees that it *is* a report).
(See Downloading the application below to run the example yourself).

    def b = new SimpleReportBuilder()
    def r = b.eval(repScript.text).init(sql: sql)
    assert r instanceof SimpleReport
    def p = r.execute(optParams)

# Architecture

A complete system would contain the following pieces.
-   **Client       :** This might be a web browser or a script that runs in Job Submission.
-   **Front End    :** This might be a Grails application; its job is to communicate between the client and the Report Engine.  For example, one job that falls to the front end is converting the list of parameters into an HTML form in a web application.
-   **Report Engine:** The report engine fetches the relevant report, parameter list, and parameter definitions from the Report Store, queries the front end for the parameters, and returns the results to the Front End.
-   **Report Store :** This is probably a version-controlled directory or set of directories in the file system, containing the source for parameters, parameter lists, reports, and Jasper Reports jrxml files.

<table border="2" cellspacing="0" cellpadding="6" rules="groups" frame="hsides">


<colgroup>
<col  class="left" />

<col  class="left" />

<col  class="left" />
</colgroup>
<thead>
<tr>
<th scope="col" class="left">Client</th>
<th scope="col" class="left">Front End</th>
<th scope="col" class="left">Report Engine</th>
</tr>
</thead>

<tbody>
<tr>
<td class="left">Request Report &rarr;</td>
<td class="left">&rarr; Request report &rarr;</td>
<td class="left">&#xa0;</td>
</tr>


<tr>
<td class="left">&#xa0;</td>
<td class="left">&#xa0;</td>
<td class="left">Fetch report definition from storage &rarr;</td>
</tr>


<tr>
<td class="left">&#xa0;</td>
<td class="left">&#xa0;</td>
<td class="left">Get parameter specification</td>
</tr>


<tr>
<td class="left">&#xa0;</td>
<td class="left">&#xa0;</td>
<td class="left">&larr; Return parameter specification to front end</td>
</tr>


<tr>
<td class="left">&#xa0;</td>
<td class="left">&larr; Generate parameter form</td>
<td class="left">&#xa0;</td>
</tr>


<tr>
<td class="left">Return parameters &rarr;</td>
<td class="left">&#xa0;</td>
<td class="left">&#xa0;</td>
</tr>


<tr>
<td class="left">&#xa0;</td>
<td class="left">&rarr; Return parameters to engine &rarr;</td>
<td class="left">&#xa0;</td>
</tr>


<tr>
<td class="left">&#xa0;</td>
<td class="left">&#xa0;</td>
<td class="left">Validate Parameters</td>
</tr>


<tr>
<td class="left">&#xa0;</td>
<td class="left">&#xa0;</td>
<td class="left">Get Report Data</td>
</tr>


<tr>
<td class="left">&#xa0;</td>
<td class="left">&#xa0;</td>
<td class="left">&larr; Return raw report data to front end</td>
</tr>


<tr>
<td class="left">&#xa0;</td>
<td class="left">&larr; Format report data for client &larr;</td>
<td class="left">&#xa0;</td>
</tr>


<tr>
<td class="left">Display data</td>
<td class="left">&#xa0;</td>
<td class="left">&#xa0;</td>
</tr>
</tbody>
</table>

# Appendices

## Downloading the application

My demonstration is in the java/lib/simple\_report repository on the [GitBlit server](https://inb01.sunyjcc.edu/git/).  You can go directly to the summary page here.

<https://inb01.sunyjcc.edu/git/summary/java!lib!simple_report.git>

It's probably better to take the url from the **repository url** listed on this page, but you can clone it with the following command (the difference is that GitBlit puts your user name in its link).

    git clone https://inb01.sunyjcc.edu/git/git/java/lib/simple_report.git

To build the project, run `build.cmd` and it should create the jar files and groovydoc for you.  It will download [Gradle](http://www.gradle.org/) the first time you run it.
