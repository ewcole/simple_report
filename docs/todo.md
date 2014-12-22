<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#sec-1">Pre-production</a>
<ul>
<li><a href="#sec-1-1"><span class="todo DONE">DONE</span> Make it compatible with Grails</a></li>
<li><a href="#sec-1-2"><span class="todo DONE">DONE</span> Resource Loader</a></li>
</ul>
</li>
<li><a href="#sec-2">Version 0.1.0</a>
<ul>
<li><a href="#sec-2-1">Parameters</a>
<ul>
<li><a href="#sec-2-1-1"><span class="todo DONE">DONE</span> Source Factory</a></li>
<li><a href="#sec-2-1-2"><span class="todo TODO">TODO</span> Report Object Factory</a></li>
<li><a href="#sec-2-1-3"><span class="todo DONE">DONE</span> Create report object values</a></li>
<li><a href="#sec-2-1-4"><span class="done CANCELED">CANCELED</span> Client</a></li>
</ul>
</li>
</ul>
</li>
<li><a href="#sec-3">Version 0.2.0</a>
<ul>
<li><a href="#sec-3-1"><span class="todo TODO">TODO</span> Create reload button in developer page</a></li>
<li><a href="#sec-3-2"><span class="todo TODO">TODO</span> New home page</a></li>
<li><a href="#sec-3-3"><span class="todo TODO">TODO</span> Improve developer page</a></li>
<li><a href="#sec-3-4"><span class="todo TODO">TODO</span> Inheritance</a></li>
</ul>
</li>
<li><a href="#sec-4">Version 0.3.0</a>
<ul>
<li><a href="#sec-4-1"><span class="todo TODO">TODO</span> Integrate with menu system</a></li>
<li><a href="#sec-4-2"><span class="todo TODO">TODO</span> Lists of Values</a></li>
<li><a href="#sec-4-3"><span class="todo TODO">TODO</span> Validation</a></li>
</ul>
</li>
</ul>
</div>
</div>

This is now an official MIS project; it will be used as a generator for CSV files, as well as a parameter form generator for Jasper Reports.

# Pre-production

## DONE Make it compatible with Grails

Make sure it will run with a Grails front end or plugin.  That might involve making the move to Grails 2

## DONE Resource Loader

The resource loader will plug into the SimpleReportBuilder and load objects by name as they are requested.

# Version 0.1.0

  Version 0.1.0 must have the basic parameter functionality fully functional.  We will initially target command line scripts and Grails.
We will not attempt to allow validation, inheritance, or chains of source factories yet.

## Parameters

### DONE Source Factory

Created a SourceFactory class to simplify implementation of different kinds of SourceFactory.

-   DONE FileSourceFactory

-   TODO SourcePathSourceFactory

    This should accept a string representing a path representing different source sources and convert them into a chain of SourceFactories.  The first factory in the list that can provide an object of the requested type is the winner.  
    
    This will be the default type of SourceFactory.

### TODO Report Object Factory

-   DONE Get the source code from a factory

-   DONE Convert the source code into report objects.

-   DONE Cache report object definitions

    Creating the objects will be an expensive process, so let's save the results of our queries.

-   CANCELED Create parameter list from JRXML

    **Defer this to a later version.**
    
    Pull out all /jasperReport/parameter elements from the jrxml and create parameters of the appropriate type for them.  Look for an existing parameter of the same name and use that if it is compatible.

-   TODO Create invocation for objects

### DONE Create report object values

These are not cached.  This will be done in the individual object types.

-   DONE ParamValue

-   DONE ParamFormValue

### CANCELED Client

Defer this to a later version.

We need a class that handles interaction with client environments.  Duties include displaying a parameter form, validating data.  In the future, it might also take on the responsibility of executing queries and displaying the results.

# Version 0.2.0

## TODO Create reload button in developer page

## TODO New home page

## TODO Improve developer page

## TODO Inheritance

# Version 0.3.0

## TODO Integrate with menu system

## TODO Lists of Values

## TODO Validation
