package edu.sunyjcc.simple_report

/** Test running each kind of application. */
public class RunnableTest extends GroovyTestCase {
  
  void printBanner(String s) {
    println ""
    println ""
    println ("*" * 79)
    println "******** $s ********************"
    println ("*" * 79)
  }

  void hr() {
    println ("*" * 79);
  }
  
    /** Get the source directory for the parameter forms, reports, etc. */
  File getSourceDir() {
    File f = new File('src/samples/dir1');
  }

  /** Get a FileSourceFactory with the source directory as its root. */
  FileSourceFactory getFileSourceFactory() {
    def fsf = new FileSourceFactory(getSourceDir())
    assert fsf
    return fsf
  }

  ReportObjectFactory getReportObjectFactory() {
    def fsf = getFileSourceFactory();
    def sp = new SystemParams();
    sp.company = "Zorg Industries"
    sp.ceo     = "Emmanuel Zorg"
    sp.profits = {-> 10 ** 15}
    def ce = new ClientEnv();
    ce.systemParams = sp;
    def rof = new ReportObjectFactory(fsf, ce);
    assert rof;
    assert rof.cache
    rof;
  }

  SimpleReport getReport(String name) {
    getReportObjectFactory().getReport(name)
  }

  void testRunReportWithSytemParams() {
    printBanner "testRunReportWithSystemParams"
    def r = getReport('system_parameters');
    hr();
    assert r;
    assert r instanceof SimpleReport
    assert r.reportObjectFactory.clientEnv.systemParams?.size() == 3
    println "Report ${r.name}"
    println r.source;
    hr();
    r.reportObjectFactory.clientEnv.systemParams?.each {
      println it;
    }
    assert r.reportObjectFactory.clientEnv.systemParams.size()
    hr();
    ParamForm rp = r.params;
    def pfv = r.getParamFormValue()
    //assert rp?.reportObjectFactory?.clientEnv;
    assert pfv;
    assert pfv.clientEnv;
    assert pfv.clientEnv.systemParams.size()
    assert pfv.systemParams;
    println pfv.systemParams;
    assert pfv.systemParams.size()
    assert pfv.getValueMap() == [profits:    10 ** 15,
                                 company:    "Zorg Industries",
                                 ceo:        "Emmanuel Zorg",];
    assert pfv.getValues().inject([:]) {
      m, vv ->
      m << [(vv.key): vv.value.value]
    } == [profits:    10 ** 15,
          company:    "Zorg Industries",
          ceo:        "Emmanuel Zorg",]
    def s = new StringWriter();
    assert r.run(OutputFormat.csv, pfv, s);
    println "$s";
    assert s?.toString().size();
    def answers = [param_name: "param_value",
                   "params.size()":  "3",
                   profits:    10 ** 15,
                   company:    "Zorg Industries",
                   ceo:        "Emmanuel Zorg",].collect {
      key, value ->
      /"$key","$value"/
    }
    assert s.toString().readLines() == answers;
  }

  void testReportWithParameters() {
    printBanner "testReportWithParameters"
    def f = getReportObjectFactory()
    def r = f.eval("""
      report(name: 'r') {
        param(name: 'year', type: "NUMBER", default: 1985){}
        data_generator(closure: {
          column(name: 'year');
          column(name: 'ceo');
          row(year: params.year, ceo: params.ceo);
        })
      }""");
    assert r instanceof SimpleReport
    def pfv = r.getParamFormValue()
    assert pfv
    def s = new StringWriter();
    assert r.run(OutputFormat.csv, pfv, s);
    println "$s";
    def answers = [["year", "ceo"],
                   [1985, "Emmanuel Zorg"]].collect {
      /"${it[0]}","${it[1]}"/
    }
    assert s.toString().readLines() == answers;
  }

  void testSimpleJob() {
    printBanner "testSimpleJob"
    ReportObjectFactory f = getReportObjectFactory();
    assert f
    assert f.clientEnv.systemParams.list().size()
    def job = f.getReportObject('job','hello_world');
    assert job
    assert job instanceof SimpleJob;
    assert job.reportObjectFactory.clientEnv.systemParams;
    def sp = job.reportObjectFactory.clientEnv.systemParams;
    assert sp.list().size()
    def pfv = job.getParamFormValue()
    assert pfv.clientEnv?.systemParams?.list()?.size()
    def pfve = pfv.export();
    assert pfve.whom == [name:        'whom',
                         type:        'STRING',
                         description: 'whom',
                         label:       'Whom shall I greet?',
                         default:     null,
                         value:       null];
    assert pfve.profits == [name:              'profits',
                            type:              'SYSTEM',
                            description:       'profits',
                            label:             'profits',
                            default:           1000000000000000];
    assert pfve.company == [name:              'company',
                            type:              'SYSTEM',
                            description:       'company',
                            label:             'company',
                            default:           'Zorg Industries'];
    assert pfve.ceo == [name:                  'ceo',
                        type:                  'SYSTEM',
                        description:           'ceo',
                        label:                 'ceo',
                        default:               'Emmanuel Zorg']
    assert pfve.keySet().collect{it} == ['whom', 'profits', 'company', 'ceo']
    assert pfve == [whom:[name:        'whom',
                          type:        'STRING',
                          description: 'whom',
                          label:       'Whom shall I greet?',
                          default:     null,
                          value:       null],
                    profits:[name:              'profits',
                             type:              'SYSTEM',
                             description:       'profits',
                             label:             'profits',
                             default:           1000000000000000],
                    company:[name:              'company',
                             type:              'SYSTEM',
                             description:       'company',
                             label:             'company',
                             default:           'Zorg Industries'],
                    ceo:[name:                  'ceo',
                         type:                  'SYSTEM',
                         description:           'ceo',
                         label:                 'ceo',
                         default:               'Emmanuel Zorg']]
    assert pfv.values.ceo
    def s = new StringWriter();
    assert job.run(OutputFormat.html, pfv, s)
    println "$s";
    assert s.toString() =~ /ceo=Emmanuel Zorg/;
  }

  void testSqlQuery() {
    printBanner "testSqlQuery"
    ReportObjectFactory f = getReportObjectFactory();
    assert f
    assert f.clientEnv.systemParams.list().size()
    def r = f.getSql("parameters")
    assert r instanceof SimpleReport
    def p = r.params
    assert p.reportObjectFactory == f
    def pfv = r.getParamFormValue();
    assert pfv;
    assert pfv.values.ceo.value == 'Emmanuel Zorg'
    def s = new StringWriter();
    // assert r.run(OutputFormat.html, pfv, s)
    // println "$s";
    // assert s.toString() =~ /ceo=Emmanuel Zorg/;
  }

  void testSqlQueryWithSystemParams() {
    printBanner "testSqlQueryWithSystemParams"
    // assert 1 == 2
    ReportObjectFactory f = getReportObjectFactory();
    assert f
    assert f.clientEnv.systemParams.list().size()
  }
  
  void testJasperReport() {
    printBanner "testJasperReport"
  }
}
