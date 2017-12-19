package edu.sunyjcc.simple_report


/** Test the Param class. */
public class ParamTest extends GroovyTestCase {
  
  void printBanner(String s) {
    println ""
    println ""
    println ("*" * 79)
    println "******** $s ********************"
    println ("*" * 79)
  }

  /** Get the source directory for the parameter forms, reports, etc. */
  File getSourceDir() {
    //println 'Before Getting Source Dir'
    File f = new File('src/samples/dir1');
    println "Source dir = $f"
    assert f
    assert f.exists()
    return f
  }

  /** Get a FileSourceFactory with the source directory as its root. */
  FileSourceFactory getFileSourceFactory() {
    def d = getSourceDir();
    assert d;
    def fsf = new FileSourceFactory(d)
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

  void testParamCreate() {
    println "******** testParamCreate ********************"
    def a = new SimpleReportBuilder()
    def p = a.param(name: 'scoobydoo', default: 'scared')
    def pe = p.export()
    println pe
    assert pe == [name:        "scoobydoo", 
                  type:        "STRING", 
                  description: "scoobydoo", 
                  default:     "scared",
                  label:       "scoobydoo"]
    assert p.export() == [name:        "scoobydoo", 
                          type:        "STRING", 
                          description: "scoobydoo", 
                          'default':     "scared",
                          label:       "scoobydoo"]
  }

  void testCreateParamForm() {
    println "******** testCreateParamForm ********************"
    def params = new SimpleReportBuilder().params {
      param(name: 'term_code', default: '201312')
    }
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'STRING', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   '201312']
    ]
    println params.export()
  }

  void testSetParamFormValues() {
    println "******** testSetParamFormValues ********************"
    def params = new SimpleReportBuilder().params {
      param(name: 'term_code', default: '201312')
    }
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'STRING', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   '201312']
    ]
    println params.export()
    //params.setParamValues(term_code: '201312')
    println (params.export())
    //assert params.term_code.currentValue == '201312'
    assert params.export() == [
      term_code: [name:        'term_code', 
                  type:        'STRING', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   '201312']
    ]
  }

  void testGetParamFormValues() {
    println "******** testGetParamFormValues ********************"
    def params = new SimpleReportBuilder().params {
      param(name: 'term_code', default: '201312')
      param(name: 'subject',   default: 'ART')
    }
    assert params.export() == [
      subject: [name:        'subject', 
                type:        'STRING', 
                description: 'subject', 
                label:       'subject', 
                "default":   'ART'],
      term_code: [name:        'term_code', 
                  type:        'STRING', 
                  description: 'term_code', 
                  label:       'term_code', 
                  "default":   '201312']
    ]
    println params.export()
    //def gv = params.getValues()
    //println "gv=${params.getValues()}"
    //assert gv == [term_code: '201312', subject: 'ART']
  }

  void testNumberParamCreate() {
    println "******** testNumberParamCreate ********************"
    def a = new SimpleReportBuilder()
    def p = a.param(name: 'scoobydoo', type: 'NUMBER', 'default': 42)
    def pe = p.export()
    println pe
    assert pe == [name:        "scoobydoo", 
                  type:        "NUMBER", 
                  description: "scoobydoo", 
                  default:     42,
                  label:       "scoobydoo"]
    assert p.export() == [name:        "scoobydoo", 
                          type:        "NUMBER", 
                          description: "scoobydoo", 
                          'default':     42,
                          label:       "scoobydoo"]
    p = a.param(name: 'scoobydoo', type: 'NUMBER', 'default': "42")
    assert p.export() == pe
  }

  
  void testHashConstructor() {
    println "********** testHashConstructor **********"
    Param p = new Param(name: 'a', label: 'parameter a');
    def pe = p.export();
    assert pe == [name: 'a',
                  type: 'STRING',
                  description: null,
                  label: 'parameter a',
                  "default":null]
  }
  
  void testCreateFromOtherParam() {
    println "******** testCreateFromOtherParam ********************"
    def a = new SimpleReportBuilder()
    def sp = a.param(name: 'scoobydoo', type: 'NUMBER', 'default': 42)
    def spe = sp.export()
    Param p = new Param(sp);
    def pe = p.export();
    println pe
    assert pe == [name:        "scoobydoo", 
                  type:        "NUMBER", 
                  description: "scoobydoo", 
                  default:     42,
                  label:       "scoobydoo"]
    assert p.export() == [name:        "scoobydoo", 
                          type:        "NUMBER", 
                          description: "scoobydoo", 
                          'default':     42,
                          label:       "scoobydoo"]
    p = a.param(name: 'scoobydoo', type: 'NUMBER', 'default': "42")
    assert p.export() == pe
  }

  void testCreateParamWithCopyFrom() {
    println "********** testCreateParamWithCopyFrom **********"
    def a = new SimpleReportBuilder(reportObjectFactory)
    def p1 = a.param(name: 'scoobydoo', default: 'scared')
    def p1e = p1.export();
    println "p1=$p1e"
    def p2 = a.param(name: 'scoobydoo', copyFrom: 'scoobydoo');
    def p2e = p2.export();
    println "p2 = $p2e";
    assert p1e == p2e;
    def p3 = a.param(name: 'shaggy', copyFrom: 'scoobydoo',
                     type: ParamType.number, label: 'Shaggy', default: 42)
    p2e.name = 'shaggy';
    p2e.type = 'NUMBER';
    p2e.label = 'Shaggy';
    p2e.description = 'shaggy';
    p2e.default = 42;
    assert p3.export() == p2e;
  }
}
