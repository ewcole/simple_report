This project is a report builder that can be used from Java or Groovy Applications.

# Branches

## Production

prod

## Master

master

## Feature branches

-   **params:** Feature branch for the collection and validation of parameters.

# Parameters

The first functional part of the system will be a scheme to collect parameters from the users and pass them on to the apps that request them.  The work will be divided into parameter definitions and parameter values.

## Define parameters and parameter sets.

This work is handled by [Param.groovy](.\src\main\groovy\edu\sunyjcc\simple_report\Param.groovy), which defines the individual parameters, [ParamList.groovy](.\src\main\groovy\edu\sunyjcc\simple_report\ParamList.groovy), which defines lists of parameters, and [ParamType.groovy](.\src\main\groovy\edu\sunyjcc\simple_report\ParamType.groovy), which defines parameter types.

### Validation

The parameters will define methods for validating individual parameters and lists of parameters.

### Re-Use

We will be able to load pre-defined parameters from the environment.  We can build an inheritance hierarchy between parameters.

### Lists of values

A parameter can provide functions that list valid values.

## Collect the parameters for an application

### Present a parameter form to the client

The parameter form will take a different form depending on the client environment.  This will have to be a set of adapters for each environment.

### Perform client-side validation

Some clients (such as web browsers) have the ability to validate data before returning it to the server.

### Deliver validated parameters to an application

This would be a second adapter, based on the type of application called.
