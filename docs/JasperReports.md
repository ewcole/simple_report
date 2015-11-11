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

# Modifications

## TODO Create an object to represent Jasper Reports report

This must implement the Runnable interface.

## Modify the ReportObjectFactory

### TODO Add a method, `getJasperReport()` to return a callable Jasper Report object

### TODO Modify getInvocation() to return invocable object for j. report
