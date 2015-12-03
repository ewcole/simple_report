One of the original goals of this project was to run Jasper Reports.

# Dependencies

We need to add the following dependencies to our code to include the Jasper Reports libraries.

## Repositories

We need to add two repositories to get all of the dependencies from Jasper Reports.

-   **<http://jaspersoft.artifactoryonline.com/jaspersoft/third-party-ce-artifacts/>:** This pulls in artifacts that the JasperSoft people had to modify from their standard versions.
-   **<http://jasperreports.sourceforge.net/maven2/>:** This pulls in the modules written by JasperSoft.

## Artifacts

We need to use the following artifacts to run Jasper Reports.

<table border="2" cellspacing="0" cellpadding="6" rules="groups" frame="hsides">


<colgroup>
<col  class="left" />

<col  class="left" />

<col  class="right" />

<col  class="left" />
</colgroup>
<thead>
<tr>
<th scope="col" class="left">Group</th>
<th scope="col" class="left">Artifact</th>
<th scope="col" class="right">Version</th>
<th scope="col" class="left">Contents</th>
</tr>
</thead>

<tbody>
<tr>
<td class="left">net.sf.jasperreports</td>
<td class="left">jasperreports</td>
<td class="right">6.2.0</td>
<td class="left">The JasperReports libraries</td>
</tr>


<tr>
<td class="left">org.mozilla</td>
<td class="left">rhino</td>
<td class="right">1.7.7</td>
<td class="left">The Rhino Javascript library</td>
</tr>
</tbody>
</table>

Rhino is needed because JasperReports can include JavaScript code.

# How to Run Jasper Reports from Java

You can call Jasper Reports from Java or Groovy code by making calls to the 
[JasperReports API](http://jasperreports.sourceforge.net/api/).  Some of the important ones are listed here.

-   **[net.sf.jasperreports.engine.JasperCompileManager](http://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperCompileManager.html):** This is a facade class for converting Jasper Reports to and from the XML format.
-   **[net.sf.jasperreports.engine.JasperReport](http://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html):** This is the compiled form of the report.
-   **[net.sf.jasperreports.engine.JasperFillManager](http://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperFillManager.html):** This is used to apply the data to the report definition, giving you a JasperPrint object.
-   **[net.sf.jasperreports.engine.JasperPrint](http://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html):** This is the report with all of the data filled in, ready to be converted into one of the final formats
-   **[net.sf.jasperreports.export.Exporter](http://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/Exporter.html):** This converts the filled-in report to the desired format.

## So, how do you run a report?

First, compile the report using the [JasperCompileManager](http://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperCompileManager.html).
Next, use the [JasperFillManager](http://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperFillManager.html) to run the report and produce a [JasperPrint](http://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html).
Finally, Convert it to the desired output format using an [Exporter](http://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/Exporter.html).

# Modifications

## Create an object to represent Jasper Reports report

JasperReport, implements Runnable

## Modify the ReportObjectFactory

### DONE Add a method, `getJasperReport()` to return a callable Jasper Report object

### DONE Modify getInvocation() to return invocable object for j. report

### TODO Create an exporter class for rendering the report to its final form.
