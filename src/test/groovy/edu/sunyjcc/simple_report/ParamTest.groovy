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
                  label:       "Scoobydoo"]
    assert p.export() == [name:        "scoobydoo", 
                          type:        "STRING", 
                          description: "scoobydoo", 
                          'default':     "scared",
                          label:       "Scoobydoo"]
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
                  label:       'Term Code', 
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
                  label:       'Term Code', 
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
                  label:       'Term Code', 
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
                label:       'Subject', 
                "default":   'ART'],
      term_code: [name:        'term_code', 
                  type:        'STRING', 
                  description: 'term_code', 
                  label:       'Term Code', 
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
                  label:       "Scoobydoo"]
    assert p.export() == [name:        "scoobydoo", 
                          type:        "NUMBER", 
                          description: "scoobydoo", 
                          'default':     42,
                          label:       "Scoobydoo"]
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
                  label:       "Scoobydoo"]
    assert p.export() == [name:        "scoobydoo", 
                          type:        "NUMBER", 
                          description: "scoobydoo", 
                          'default':     42,
                          label:       "Scoobydoo"]
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
    def p4 = a.param(name: 'shaggy1', copyFrom: 'shaggy');
    def p4e = p4.export();
    assert p4e.label == "Shaggy's State";
    assert p4.getLabel() == p4.label
    assert p4.label == "Shaggy's State"
    println "p4 = $p4e";
    println "****************************************"
    println "Test labels"
    def p5 = a.param(name: 'scooby_doo', copyFrom: 'scoobydoo');
    assert p5.label == 'Scooby Doo'
    def p6 = a.param(name: 'scooby_doo', copyFrom: 'scoobydoo', label: "P6");
    assert p6.label == 'P6'
    def p7 = a.param(name: 'shaggy', copyFrom: 'shaggy');
    assert p7.label == "Shaggy's State"
    def p8 = a.param(name: 'shaggy', copyFrom: 'shaggy', label: "P8");
    assert p8.label == "P8"

  }

  void testCopyFromMissingParameter() {
    printBanner "testCopyFromMissingParameter"
    println "Create a parameter with a copyFrom that points "
    println "    to a non-existent param."
    def factory = reportObjectFactory
    def a = new SimpleReportBuilder(factory)
    def p1;
    try {
      p1 = factory.getParam('non_existent_param');
    } catch (BuildException e) {
      // Everything's fine.
    }
    assert p1 == null;
    def p2 = null;
    try {
      p2 = a.param(name: 'zorg', copyFrom: 'non_existent_param');
    } catch(BuildException e) {
      println "Could not create without parameter"
    }
    assert !p2
  }
}
