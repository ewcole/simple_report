package edu.sunyjcc.simple_report


/** Test the FileSourceFactory class. */
public class FileSourceFactoryTest extends GroovyTestCase {

  File getSourceDir() {
    File f = new File('samples/dir1');
  }

  void testFileSourceFactoryCreate() {
    println "******** testFileSourceFactoryCreate ********************";
    File f = getSourceDir();
    assert f
    assert f.exists()
    FileSourceFactory fsf = new FileSourceFactory(f);
    assert fsf
    assert fsf.sourceRoot
    assert fsf.sourceRoot.exists()
  }

  void testGetParam() {
    def f = getSourceDir();
    def fsf = new FileSourceFactory(f);
    def paramDir = new File(f, 'param');
    File paramDef = new File(paramDir, 'scoobydoo.groovy')
    assert paramDef.exists()
    def scoobyStr = "param(name: 'scoobydoo', default: 'scared')";
    assert paramDef.text == scoobyStr;
    def s = fsf.getParamSource('scoobydoo')
    assert fsf.sourceRoot.exists()
    assert s == scoobyStr
  }
}