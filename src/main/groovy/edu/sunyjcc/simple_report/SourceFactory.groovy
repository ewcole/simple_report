package edu.sunyjcc.simple_report

/** Objects that implement this interface will have methods that return the 
 *  source code for various objects defined in the simple_reports library.
 *  SourceFactory objects are not responsible for caching; that will be done 
 *  elsewhere.
 */
public interface SourceFactory {
  /** Get the source for a parameter object */
  String getParamSource(String name);
  /** Get the source for a parameter form object */
  String getParamFormSource(String name);
  /** Get a .jrxml file, defining a Jasper Report. */
  String getJrxmlSource(String name);
}
