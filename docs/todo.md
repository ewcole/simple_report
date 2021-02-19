<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#orgheadline3">Pre-production</a>
<ul>
<li><a href="#orgheadline1"><span class="todo nilDONE">DONE</span> Make it compatible with Grails</a></li>
<li><a href="#orgheadline2"><span class="todo nilDONE">DONE</span> Resource Loader</a></li>
</ul>
</li>
<li><a href="#orgheadline18">Version 0.1.0</a>
<ul>
<li><a href="#orgheadline16">Parameters</a>
<ul>
<li><a href="#orgheadline6"><span class="todo nilDONE">DONE</span> Source Factory</a></li>
<li><a href="#orgheadline11"><span class="todo nilDONE">DONE</span> Report Object Factory</a></li>
<li><a href="#orgheadline14"><span class="todo nilDONE">DONE</span> Create report object values</a></li>
<li><a href="#orgheadline15"><span class="done nilCANCELED">CANCELED</span> Client</a></li>
</ul>
</li>
<li><a href="#orgheadline17"><span class="done nilCANCELED">CANCELED</span> Create parameter list from JRXML</a></li>
</ul>
</li>
<li><a href="#orgheadline25">Pre-Production</a>
<ul>
<li><a href="#orgheadline20">Version 0.2.0</a>
<ul>
<li><a href="#orgheadline19"><span class="todo nilDONE">DONE</span> Auto documentation</a></li>
</ul>
</li>
<li><a href="#orgheadline22">Version 0.3.0</a>
<ul>
<li><a href="#orgheadline21"><span class="todo nilDONE">DONE</span> Integrate with menu system</a></li>
</ul>
</li>
<li><a href="#orgheadline24">Version 0.4.0</a>
<ul>
<li><a href="#orgheadline23"><span class="todo nilTODO">TODO</span> Lists of Values</a></li>
</ul>
</li>
</ul>
</li>
<li><a href="#orgheadline29">Version 1</a>
<ul>
<li><a href="#orgheadline26">Version 1.2.1</a></li>
<li><a href="#orgheadline28">Version 1.3.0</a>
<ul>
<li><a href="#orgheadline27"><span class="todo nilTODO">TODO</span> Run Jasper Reports</a></li>
</ul>
</li>
</ul>
</li>
<li><a href="#orgheadline33">Future Versions</a>
<ul>
<li><a href="#orgheadline30"><span class="todo nilTODO">TODO</span> Inheritance</a></li>
<li><a href="#orgheadline31"><span class="todo nilTODO">TODO</span> Validation</a></li>
<li><a href="#orgheadline32"><span class="todo nilTODO">TODO</span> Run Jobs</a></li>
</ul>
</li>
</ul>
</div>
</div>

This is now an official MIS project; it will be used as a generator for CSV files, as well as a parameter form generator for Jasper Reports.

# Pre-production<a id="orgheadline3"></a>

## DONE Make it compatible with Grails<a id="orgheadline1"></a>

Make sure it will run with a Grails front end or plugin.  That might involve making the move to Grails 2

## DONE Resource Loader<a id="orgheadline2"></a>

The resource loader will plug into the SimpleReportBuilder and load objects by name as they are requested.

# Version 0.1.0<a id="orgheadline18"></a>

  Version 0.1.0 must have the basic parameter functionality fully functional.  We will initially target command line scripts and Grails.
We will not attempt to allow validation, inheritance, or chains of source factories yet.

## Parameters<a id="orgheadline16"></a>

### DONE Source Factory<a id="orgheadline6"></a>

Created a SourceFactory class to simplify implementation of different kinds of SourceFactory.

-   DONE FileSourceFactory

-   TODO SourcePathSourceFactory

    This should accept a string representing a path representing different source sources and convert them into a chain of SourceFactories.  The first factory in the list that can provide an object of the requested type is the winner.  
    
    This will be the default type of SourceFactory.

### DONE Report Object Factory<a id="orgheadline11"></a>

-   DONE Get the source code from a factory

-   DONE Convert the source code into report objects.

-   DONE Cache report object definitions

    Creating the objects will be an expensive process, so let's save the results of our queries.

-   DONE Create invocation for objects

### DONE Create report object values<a id="orgheadline14"></a>

These are not cached.  This will be done in the individual object types.

-   DONE ParamValue

-   DONE ParamFormValue

### CANCELED Client<a id="orgheadline15"></a>

Defer this to a later version.

We need a class that handles interaction with client environments.  Duties include displaying a parameter form, validating data.  In the future, it might also take on the responsibility of executing queries and displaying the results.

## CANCELED Create parameter list from JRXML<a id="orgheadline17"></a>

**Defer this to a later version.**

Pull out all /jasperReport/parameter elements from the jrxml and create parameters of the appropriate type for them.  Look for an existing parameter of the same name and use that if it is compatible.

# Pre-Production<a id="orgheadline25"></a>

## Version 0.2.0<a id="orgheadline20"></a>

### DONE Auto documentation<a id="orgheadline19"></a>

## Version 0.3.0<a id="orgheadline22"></a>

### DONE Integrate with menu system<a id="orgheadline21"></a>

## Version 0.4.0<a id="orgheadline24"></a>

### TODO Lists of Values<a id="orgheadline23"></a>

# Version 1<a id="orgheadline29"></a>

## Version 1.2.1<a id="orgheadline26"></a>

## Version 1.3.0<a id="orgheadline28"></a>

### TODO Run Jasper Reports<a id="orgheadline27"></a>

Run Jasper reports; take parameter form from parameter form defs.

# Future Versions<a id="orgheadline33"></a>

## TODO Inheritance<a id="orgheadline30"></a>

## TODO Validation<a id="orgheadline31"></a>

## TODO Run Jobs<a id="orgheadline32"></a>
