package edu.sunyjcc.simple_report;

/** This interface guarantees that we have a function to convert implementing
 *  objects into Groovy hashes, lists and simple objects.
 */
public interface Exportable {
  /** Return a simple Groovy object with all the important information about 
   *  the implementing object.
   */
  def export();
}