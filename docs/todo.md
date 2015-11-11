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
<li><a href="#sec-2-1-2"><span class="todo DONE">DONE</span> Report Object Factory</a></li>
<li><a href="#sec-2-1-3"><span class="todo DONE">DONE</span> Create report object values</a></li>
<li><a href="#sec-2-1-4"><span class="done CANCELED">CANCELED</span> Client</a></li>
</ul>
</li>
<li><a href="#sec-2-2"><span class="done CANCELED">CANCELED</span> Create parameter list from JRXML</a></li>
</ul>
</li>
<li><a href="#sec-3">Pre-Production</a>
<ul>
<li><a href="#sec-3-1">Version 0.2.0</a>
<ul>
<li><a href="#sec-3-1-1"><span class="todo DONE">DONE</span> Auto documentation</a></li>
</ul>
</li>
<li><a href="#sec-3-2">Version 0.3.0</a>
<ul>
<li><a href="#sec-3-2-1"><span class="todo DONE">DONE</span> Integrate with menu system</a></li>
</ul>
</li>
<li><a href="#sec-3-3">Version 0.4.0</a>
<ul>
<li><a href="#sec-3-3-1"><span class="todo TODO">TODO</span> Lists of Values</a></li>
</ul>
</li>
</ul>
</li>
<li><a href="#sec-4">Version 1</a>
<ul>
<li><a href="#sec-4-1">Version 1.2.1</a></li>
<li><a href="#sec-4-2">Version 1.3.0</a>
<ul>
<li><a href="#sec-4-2-1"><span class="todo TODO">TODO</span> Run Jasper Reports</a></li>
</ul>
</li>
</ul>
</li>
<li><a href="#sec-5">Future Versions</a>
<ul>
<li><a href="#sec-5-1"><span class="todo TODO">TODO</span> Inheritance</a></li>
<li><a href="#sec-5-2"><span class="todo TODO">TODO</span> Validation</a></li>
</ul>
</li>
</ul>
</div>
</div>

This is now an official MIS project; it will be used as a generator for CSV files, as well as a parameter form generator for Jasper Reports.

# Pre-production<a id="sec-1" name="sec-1"></a>

## DONE Make it compatible with Grails<a id="sec-1-1" name="sec-1-1"></a>

Make sure it will run with a Grails front end or plugin.  That might involve making the move to Grails 2

## DONE Resource Loader<a id="sec-1-2" name="sec-1-2"></a>

The resource loader will plug into the SimpleReportBuilder and load objects by name as they are requested.

# Version 0.1.0<a id="sec-2" name="sec-2"></a>

  Version 0.1.0 must have the basic parameter functionality fully functional.  We will initially target command line scripts and Grails.
We will not attempt to allow validation, inheritance, or chains of source factories yet.

## Parameters<a id="sec-2-1" name="sec-2-1"></a>

### DONE Source Factory<a id="sec-2-1-1" name="sec-2-1-1"></a>

Created a SourceFactory class to simplify implementation of different kinds of SourceFactory.

-   DONE FileSourceFactory

-   TODO SourcePathSourceFactory

    This should accept a string representing a path representing different source sources and convert them into a chain of SourceFactories.  The first factory in the list that can provide an object of the requested type is the winner.  
    
    This will be the default type of SourceFactory.

### DONE Report Object Factory<a id="sec-2-1-2" name="sec-2-1-2"></a>

-   DONE Get the source code from a factory

-   DONE Convert the source code into report objects.

-   DONE Cache report object definitions

    Creating the objects will be an expensive process, so let's save the results of our queries.

-   DONE Create invocation for objects

### DONE Create report object values<a id="sec-2-1-3" name="sec-2-1-3"></a>

These are not cached.  This will be done in the individual object types.

-   DONE ParamValue

-   DONE ParamFormValue

### CANCELED Client<a id="sec-2-1-4" name="sec-2-1-4"></a>

Defer this to a later version.

We need a class that handles interaction with client environments.  Duties include displaying a parameter form, validating data.  In the future, it might also take on the responsibility of executing queries and displaying the results.

## CANCELED Create parameter list from JRXML<a id="sec-2-2" name="sec-2-2"></a>

**Defer this to a later version.**

Pull out all /jasperReport/parameter elements from the jrxml and create parameters of the appropriate type for them.  Look for an existing parameter of the same name and use that if it is compatible.

# Pre-Production<a id="sec-3" name="sec-3"></a>

## Version 0.2.0<a id="sec-3-1" name="sec-3-1"></a>

### DONE Auto documentation<a id="sec-3-1-1" name="sec-3-1-1"></a>

## Version 0.3.0<a id="sec-3-2" name="sec-3-2"></a>

### DONE Integrate with menu system<a id="sec-3-2-1" name="sec-3-2-1"></a>

## Version 0.4.0<a id="sec-3-3" name="sec-3-3"></a>

### TODO Lists of Values<a id="sec-3-3-1" name="sec-3-3-1"></a>

# Version 1<a id="sec-4" name="sec-4"></a>

## Version 1.2.1<a id="sec-4-1" name="sec-4-1"></a>

## Version 1.3.0<a id="sec-4-2" name="sec-4-2"></a>

### TODO Run Jasper Reports<a id="sec-4-2-1" name="sec-4-2-1"></a>

Run Jasper reports; take parameter form from parameter form defs.

# Future Versions<a id="sec-5" name="sec-5"></a>

## TODO Inheritance<a id="sec-5-1" name="sec-5-1"></a>

## TODO Validation<a id="sec-5-2" name="sec-5-2"></a>
