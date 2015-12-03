package edu.sunyjcc.simple_report


/** Test the ReportObjectFactory class. */
public class ReportObjectFactoryTest extends GroovyTestCase {

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

  ReportObjectFactory getReportObjectFactory() {
    def fsf = getFileSourceFactory();
    def rof = new ReportObjectFactory(fsf);
    assert rof;
    assert rof.cache
    rof;
  }

  void printBanner(String s) {
    println ""
    println ""
    println ("*" * 79)
    println "******** $s ********************"
    println ("*" * 79)
  }

  void testCacheCreation() {
    printBanner("testCacheCreation");
    String type = 'param';
    String name = 'scoobydoo';
    println "Running test: type:'$type', name: '$name'";
    def fsf = getFileSourceFactory()
    assert fsf
    def rof = getReportObjectFactory()
    assert rof
    def src = fsf.getSource(type, name)
    assert src.size()
    def cache = rof.cache;
    println "cache = $cache"
    assert cache
    def c = cache[type]
    println "c=$c"
    assert c && c.getSrc
    println "c.keySet() == ${c.keySet()}"
    def getSrc = c.getSrc
    println "getSrc is a ${getSrc.getClass()}"
    assert getSrc
    println "getSrc=${getSrc.toString()}"
    assert getSrc
    String rofSrc = getSrc(name)
    println "rofSrc = $rofSrc"
    assert src == rofSrc
  }

  def runSrcTest(String type, String name) {
    println ("*" * 79)
    println "Running test: type:'$type', name: '$name'"
    println ("*" * 79)
    def fsf = getFileSourceFactory()
    assert fsf
    def rof = getReportObjectFactory()
    assert rof
    def src = fsf.getSource(type, name)
    assert src.size()
    //println "fsfSrc = $src"
    assert src.size()
    def cache = rof.cache;
    assert cache
    def c = rof.getTypeCache(type)
    if (!c) {
      println "Cache not found for $type!"
    } else {
      println "cache[$type] = $c"
    }
    assert c && c.getSrc
    def getSrc = c.getSrc
    assert getSrc
    assert getSrc
    String rofSrc = getSrc(name)
    //println "rofSrc = $rofSrc"
    assert src == rofSrc
    println ("*" * 79)
  }

  def createFromCache(String objType, String objName) {
    println "** Creating $objType/$objName"
    def rof = getReportObjectFactory()
    rof.getTypeCache(objType).getObject.call(objName)
  }

  void testGetParamSource() {
    printBanner("testGetParamSource");
    runSrcTest('param', 'scoobydoo')
  }

  void testGetParamFormSource() {
    printBanner("testGetParamFormSource")
    runSrcTest('param_form', "SubjectAndTerm");
  }

  void testGetParamFormSource2() {
    printBanner("testGetParamFormSource2")
    runSrcTest('paramForm', "SubjectAndTerm");
  }

  void testGetJrxmlSource() {
    printBanner("testGetJrxmlSource")
    runSrcTest('jrxml', "apps")
  }

  /** See if the create function works for parameters. */
  void testCacheCreateParam () {
    printBanner("testCacheCreateParam")
    def obj = createFromCache("param", "scoobydoo")
    println "obj=${obj.export()}"
    assert obj.export() == [name:        "scoobydoo", 
                            type:        "STRING", 
                            description: "scoobydoo", 
                            label:       "scoobydoo", 
                            default:     "scared"]
  }
  
  void testGetReportObject_param() {
    printBanner("testGetReportObject_param")
    def obj = getReportObjectFactory().getReportObject("param", "scoobydoo");
    println "obj=${obj?.export()}"
    assert obj.export() == [name:        "scoobydoo", 
                            type:        "STRING", 
                            description: "scoobydoo", 
                            label:       "scoobydoo", 
                            default:     "scared"]
  }
 
  void testGetParam() {
    printBanner("testGetParam")
    def obj = getReportObjectFactory().getParam("scoobydoo");
    println "obj=${obj?.export()}"
    assert obj.export() == [name:        "scoobydoo", 
                            type:        "STRING", 
                            description: "scoobydoo", 
                            label:       "scoobydoo", 
                            default:     "scared"]

  }

  /** See if the create function works for parameter forms. */
  void testCacheCreateParamForm () {
    printBanner("testCacheCreateParamForm")
    def obj = createFromCache("paramForm", "SubjectAndTerm")
    println "obj=${obj.export()}"
    
    assert obj.export() == [subject: [name:        "subject", 
                                      type:        "STRING", 
                                      description: "subject", 
                                      label:       "subject", 
                                      default:     "ART"], 
                            term_code:[name:        "term_code", 
                                       type:        "STRING", 
                                       description: "term_code", 
                                       label:       "term_code", 
                                       default:     "201312"]]
  
  }

  void testGetReportObject_paramForm() {
    printBanner("testGetReportObject_paramForm")
    def factory = getReportObjectFactory()
    def obj = factory.getReportObject("paramForm", 
                                      "SubjectAndTerm");
    println "obj=${obj?.export()}"
    assert obj.export() == [subject: [name:        "subject", 
                                      type:        "STRING", 
                                      description: "subject", 
                                      label:       "subject", 
                                      default:     "ART"], 
                            term_code:[name:        "term_code", 
                                       type:        "STRING", 
                                       description: "term_code", 
                                       label:       "term_code", 
                                       default:     "201312"]]
  }  

  void testGetParamForm() {
    printBanner("testGetParamForm")
    def obj = getReportObjectFactory().getParamForm("SubjectAndTerm");
    println "obj=${obj?.export()}"
    assert obj.export() == [subject: [name:        "subject", 
                                      type:        "STRING", 
                                      description: "subject", 
                                      label:       "subject", 
                                      default:     "ART"], 
                            term_code:[name:        "term_code", 
                                       type:        "STRING", 
                                       description: "term_code", 
                                       label:       "term_code", 
                                       default:     "201312"]]
  }

  /** See if the create function works for parameter forms. */
  void testCacheCreateJasperReport () {
    printBanner("testCacheCreateJasperReport")
    def obj = createFromCache("jrxml", "apps")
    println "obj=${obj}"
    def fsfSrc = getFileSourceFactory().getSource("jrxml", "apps")
    assert obj.source == fsfSrc
  }

  void testGetReport () {
    printBanner("testGetReport")
    String reportName = 'simple_report_types'
    def factory = getReportObjectFactory()
    def rpt = factory.getReportObject('report', reportName);
    assert rpt
    assert rpt instanceof SimpleReport
    println "rpt exists: $rpt"
    rpt = factory.getReport(reportName)
  }

  void testGetJrxml() {
    printBanner("testGetJrxml")
  } 

  void testGetSqlObject() {
    printBanner("testGetSqlObject");
    def factory = getReportObjectFactory()
    def s = factory.getSql("tables")
    assert s
    println "s.export() = ${s.export()}"
    assert s instanceof SimpleReport
  }

  void testGetSqlObjectWithParams() {
    printBanner("testGetSqlObjectWithParams");
    def factory = getReportObjectFactory()
    def s = factory.getSql("terms")
    assert s
    println "s.export() == ${s.export()}"
    def se = s.export()  
    assert se
    def pp = se.params;
    println "pp=$pp"
    assert pp
    assert pp.size() == 1
    assert pp.year == [name:        'year',
                       type:        'STRING',
                       description: 'year',
                       label:       'year',
                       "default":   null]
    assert s instanceof SimpleReport
  }
}