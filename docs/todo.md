<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#sec-1">1. Pre-production</a>
<ul>
<li><a href="#sec-1-1">1.1. <span class="todo TODO">TODO</span> Make it compatible with Grails</a></li>
<li><a href="#sec-1-2">1.2. <span class="todo TODO">TODO</span> Resource Loader</a></li>
</ul>
</li>
<li><a href="#sec-2">2. Version 1.0</a>
<ul>
<li><a href="#sec-2-1">2.1. Parameters</a>
<ul>
<li><a href="#sec-2-1-1">2.1.1. <span class="todo TODO">TODO</span> Source Factory</a></li>
<li><a href="#sec-2-1-2">2.1.2. <span class="todo TODO">TODO</span> Report Object Factory</a></li>
<li><a href="#sec-2-1-3">2.1.3. <span class="todo TODO">TODO</span> ClientInteraction</a></li>
</ul>
</li>
</ul>
</li>
</ul>
</div>
</div>

This is now an official MIS project; it will be used as a generator for CSV files, as well as a parameter form generator for Jasper Reports.

# Pre-production

## TODO Make it compatible with Grails

Make sure it will run with a Grails front end or plugin.  That might involve making the move to Grails 2

## TODO Resource Loader

The resource loader will plug into the SimpleReportBuilder and load objects by name as they are requested.

# Version 1.0

Version 1.0 must have the basic parameter functionality fully functional.  We will initially target command line scripts and Grails.

## Parameters

### TODO Source Factory

Created a SourceFactory class to simplify implementation of different kinds of SourceFactory.

1.  DONE FileSourceFactory

2.  TODO SourcePathSourceFactory

    This should accept a string representing a path representing different source sources and convert them into a chain of SourceFactories.  The first factory in the list that can provide an object of the requested type is the winner.  
    
    This will be the default type of SourceFactory.

### TODO Report Object Factory

1.  DONE Get the source code from a factory

2.  TODO Convert the source code into report objects.

3.  TODO Cache report object definitions

    Creating the objects will be an expensive process, so let's save the results of our queries.

4.  TODO Create report object values

    These are not cached.

### TODO ClientInteraction

We need a class that handles interaction with client environments.  Duties include displaying a parameter form, validating data.  In the future, it might also take on the responsibility of executing queries and displaying the results.
