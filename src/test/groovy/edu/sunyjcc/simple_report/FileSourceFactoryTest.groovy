package edu.sunyjcc.simple_report


/** Test the FileSourceFactory class. */
public class FileSourceFactoryTest extends GroovyTestCase {

  /** Get the source directory for the parameter forms, reports, etc. */
  File getSourceDir() {
    File f = new File('src/samples/dir1');
  }

  /** Get a subdirectory of the source directory */
  File getSourceSubDir(String subDirName) {
    File f = new File(getSourceDir(), subDirName);
  }

  /** Get a FileSourceFactory with the source directory as its root. */
  FileSourceFactory getFileSourceFactory() {
    def fsf = new FileSourceFactory(getSourceDir())
    assert fsf
    return fsf
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

  /** See if we can cast the file source factory to its base classes */
  void testFileSourceFactoryCast() {
    println "******** testFileSourceFactoryCast ********************";
    def fsf = getFileSourceFactory()
    println "fsf.getClass() == ${fsf.getClass()}"
    BaseSourceFactory bsf = (BaseSourceFactory) fsf
    println "bsf==${bsf}"
    SourceFactory sf1 = (SourceFactory)bsf;
    println "Converted BaseSourceFactory to SourceFactory (sf1=$sf1)"
    SourceFactory sf = fsf;
    println "Converted FileSourceFactory to SourceFactory (sf=$sf)"
  }

  /** Test FileSourceFactory.getParam() */
  void testGetParamSource() {
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

  /** Test FileSourceFactory.getParamForm() */
  void testGetParamFormSource() {
    def fsf = getFileSourceFactory();
    String pfName = "SubjectAndTerm"
    def paramFormFile = new File(getSourceDir(), "param_form/${pfName}.groovy")
    assert paramFormFile.exists()
    def pfText = fsf.getParamFormSource(pfName)
    assert pfText == paramFormFile.text
  }

  /** Test FileSourceFactory.getJrxml() */
  void testGetJrxmlSource() {
    def fsf = getFileSourceFactory()
    def jrxmlDir = getSourceSubDir('jrxml')
    assert jrxmlDir.exists()
    def jrxmlSource = new File(jrxmlDir, "apps.jrxml")
    def factorySource = fsf.getJrxmlSource("apps");
    assert jrxmlSource.text == factorySource
  }
}