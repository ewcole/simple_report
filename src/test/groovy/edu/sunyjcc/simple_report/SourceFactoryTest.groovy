package edu.sunyjcc.simple_report


/** Test the SourceFactory class. */
public class SourceFactoryTest extends GroovyTestCase {

  /** Get the source directory for the parameter forms, reports, etc. */
  File getSourceDir() {
    File f = new File('src/samples/dir1');
  }

  /** Get a subdirectory of the source directory */
  File getSourceSubDir(String subDirName) {
    File f = new File(getSourceDir(), subDirName);
  }

  /** Get a SourceFactory with the source directory as its root. */
  SourceFactory getSourceFactory() {
    def fsf = new FileSourceFactory(getSourceDir())
    assert fsf
    return fsf
  }

  void printBanner(String s) {
    println "******** $s ********************"
  }

  void testNormalizeParamType() {
    printBanner("testNormalizeParamType");
    def sf = getSourceFactory()
    // Be sure that the left side of each eqiv maps to the right side.
    def equivs = """
      param=param
      param_form=param_form
      paramForm=param_form
      ParamForm=param_form
      report=report
      jrxml=jrxml
      jasper=jrxml""".split(/ *\r?\n */).grep{it.size()};
    println equivs;
    equivs.each {
      equiv ->
        println "Test:  $equiv";
        def testData = equiv.split('=');
        String src = testData[0];
        String dest = testData[1];
        def rslt = sf.normalizeTypeName(src)
        println "    $src -> $dest?  $rslt"
        assert dest == rslt;
    }
  }


  void testGetReport() {
    printBanner("testGetReport");
    def sf = getSourceFactory()
    def reportName = 'simple_report_types'
    def reportDir = getSourceSubDir('report');
    assert reportDir.exists()
    def reportFile = new File(reportDir, "${reportName}.groovy")
    assert reportFile.exists()
    def txt = reportFile.text
    println "Report source: "
    println "$txt"
    println "Before getting report source: sf.getReportSource('$reportName')"
    assert sf.getReportSource(reportName) == txt
    println "Got report source."
    println "Getting report source: sf.getSourceText('report', '$reportName')"
    assert sf.getSource('report', reportName) == txt
  }

  void testList () {
    printBanner("testList")
    def sf = getSourceFactory()
    def l = sf.list()
    println l
    assert l == [[type: 'param', name:'scoobydoo'], 
                 [type: 'param_form', name: 'SubjectAndTerm'], 
                 [type: 'report', name: 'simple_report_types'],
                 [type: 'sql', name: 'tables'],
                 [type: 'sql', name: 'terms'],]
  }
}