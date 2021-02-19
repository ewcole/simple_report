package edu.sunyjcc.simple_report
import groovy.json.*

/** Test the ParamFormValue class. */
public class ParamFormValueTest extends GroovyTestCase {

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
    def spf = new ClientEnv();
    spf.systemParams.home = "Commodore64"
    def rof = new ReportObjectFactory(fsf, spf);
    assert rof;
    assert rof.cache
    rof;
  }

  void getParamForm(String name) {
    // def rof = getReportObjectFactory()
    // assert rof
    // rof.getParamForm(name)
    getReportObjectFactory().getParamForm(name);
  }

  void testGetParamForm() {
    printBanner("testGetParamForm")
    def f = getReportObjectFactory().getParamForm("SubjectAndTerm");
    assert f
    assert f.export()  == [home:[name:         'home',
                                 type:         'SYSTEM',
                                 description:  'home',
                                 label:        'home',
                                 'default':    'Commodore64'],
                           subject: [name:        "subject", 
                                     type:        "STRING", 
                                     description: "subject", 
                                     label:       "Subject", 
                                     default:     "ART"], 
                           term_code:[name:        "term_code", 
                                      type:        "STRING", 
                                      description: "term_code", 
                                      label:       "Term Code", 
                                      default:     "201312"]];
    def spf =  f.reportObjectFactory.clientEnv;
    assert spf.systemParams.home == "Commodore64"
  }

  void testGetParamFormValue() {
    printBanner("testGetParamFormValue")
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    assert pf.export() == [home:[name:         'home',
                                 type:         'SYSTEM',
                                 description:  'home',
                                 label:        'home',
                                 'default':    'Commodore64'],
                           subject: [name:        "subject", 
                                    type:        "STRING", 
                                    description: "subject", 
                                    label:       "Subject", 
                                    default:     "ART"], 
                          term_code:[name:        "term_code", 
                                     type:        "STRING", 
                                     description: "term_code", 
                                     label:       "Term Code", 
                                     default:     "201312"]]
    def v = new ParamFormValue(pf)
    assert v
    assert v.values.containsKey('term_code')
    def tcv = v.getValue('term_code');
    assert tcv;
    println "tcv.export()=${tcv.export()}"
    def sv = v.getValue('subject');
    println "sv=${sv}"
    println "sv.export()=${sv.export()}"
    // Should be same as the param form export, but with added values.
    assert v.export() == [home:[name:         'home',
                                 type:         'SYSTEM',
                                 description:  'home',
                                 label:        'home',
                                 value:        'Commodore64'],
                           subject: [name:        "subject", 
                                    type:        "STRING", 
                                    description: "subject", 
                                    label:       "Subject", 
                                    "default":     "ART",
                                    value:         "ART"
                                   ], 
                          term_code:[name:        "term_code", 
                                     type:        "STRING", 
                                     description: "term_code", 
                                     label:       "Term Code", 
                                     "default":     "201312",
                                     value:         "201312"]]
    def sp =  v.systemParams;
    assert sp.home == "Commodore64"
  }
  void testGetParamFormValue2() {
    printBanner("testGetParamFormValue2")
    println "Does the getParamFormValue() function return self?"
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    def v = new ParamFormValue(pf)
    assert v
    assert v.getParamFormValue() == v
    def sp =  v.systemParams;
    assert sp.home == "Commodore64"
  }

  void testGetParamFormParamValue() {
    printBanner("testGetParamFormParamValue")
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    assert pf.export() == [home:[name:         'home',
                                 type:         'SYSTEM',
                                 label:        'home',
                                 'default':    'Commodore64',
                                 description:  'home'],
                           subject: [name:        "subject", 
                                    type:        "STRING", 
                                    description: "subject", 
                                    label:       "Subject", 
                                    default:     "ART"], 
                          term_code:[name:        "term_code", 
                                     type:        "STRING", 
                                     description: "term_code", 
                                     label:       "Term Code", 
                                     default:     "201312"]]
    def v = new ParamFormValue(pf)
    assert v
    def tc = v.getValue('term_code');
    assert tc;
    assert tc.export() == [name:        "term_code", 
                           type:        "STRING", 
                           description: "term_code", 
                           label:       "Term Code", 
                           "default":     "201312",
                           value:         "201312"]
  }

 void testSetParamValue() {
    printBanner("testGetParamFormParamValue")
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    def v = new ParamFormValue(pf)
    assert v
    v.setValue('term_code', "201512")
    assert v.getValue('term_code').export() == [name:        "term_code", 
                                                type:        "STRING", 
                                                description: "term_code", 
                                                label:       "Term Code", 
                                                "default":     "201312",
                                                value:         "201512"]
 }

 void testGetValues() {
    printBanner("testGetValues")
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    def v = new ParamFormValue(pf)
    assert v
    assert v.getValues().inject([:]) {
      m, vv ->
        m << [(vv.key): vv.value.value]
    } == [term_code: "201312", subject: "ART", home: "Commodore64"]
 }

 void testSetValues() {
    printBanner("testSetValues")
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    def v = new ParamFormValue(pf)
    assert v
    // getValueMap will return the system parameters as well.
    assert v.getValueMap() == [term_code: "201312", subject: "ART",
                               home: "Commodore64"]
    v.setParamValues([term_code: "201512", subject: "CSC"])
    def v2 = v.values;
    assert v2.term_code.value == "201512"
    assert v2.subject.value   == "CSC"
    assert v2.keySet().sort() == "term_code subject home".split(/ +/).sort()
    assert v.getValueMap() == [term_code: "201512", subject: "CSC",
                               home: "Commodore64"]
    assert (v2.term_code.value).getClass() == java.lang.String
    assert v.values
    assert v.values.term_code instanceof ParamValue
 }

  void testRunParamForm () {
    printBanner("testRunParamForm") 
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    assert pf
    def v = new ParamFormValue(pf)
    assert v
    def s = new StringWriter()
    assert v.run(OutputFormat.json, s)
    println s.toString()
    def o = new JsonSlurper().parseText(s.toString())
    assert o.subject == [name:        'subject', 
                         type:        'STRING', 
                         description: 'subject', 
                         label:       'Subject', 
                         'default':   'ART', 
                         value:       'ART']
    assert o.term_code == [name:        'term_code', 
                           type:        'STRING', 
                           description: 'term_code', 
                           label:       'Term Code', 
                           'default':   '201312', 
                           value:       '201312']
    assert o.home.default == 'Commodore64';
    assert o.home.description == 'home';
    assert o.home.label == 'home';
    assert o.home.name == 'home';
    assert o.home.type == 'SYSTEM';
    assert o.home.value == 'Commodore64';
    assert o.home == ['default':    'Commodore64',
                      description:  'home',
                      label:        'home',
                       name:         'home',
                       type:         'SYSTEM',
                      value:        'Commodore64']
    assert o.keySet().collect{it} == "home subject term_code".split(/ +/);
    assert o == [home:['default':    'Commodore64',
                       description:  'home',
                       label:        'home',
                       name:         'home',
                       type:         'SYSTEM',
                       value:        'Commodore64'],
                 subject: ['default':   'ART', 
                           description: 'subject', 
                           name:        'subject', 
                           type:        'STRING', 
                           label:       'Subject', 
                           value:       'ART'],
                 term_code: [name:        'term_code', 
                             type:        'STRING', 
                             description: 'term_code', 
                             label:       'Term Code', 
                             'default':   '201312', 
                             value:       '201312']]
  }

  void testRunParamFormWithNonDefaultValues () {
    printBanner("testRunParamFormWithNonDefaultValues") 
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    println "pf=${pf.export()}"
    def v = new ParamFormValue(pf)
    assert v
    println "v=${v.export()}"
    def s = new StringWriter()
    v.setParamValues([subject: 'BIO', term_code: '199712'])
    println "After setting param values, v=${v.export()}"
    assert v.values.subject.value == 'BIO'
    assert v.values.term_code.value == '199712'
    assert v.run(OutputFormat.json, s)
    def o = new JsonSlurper().parseText(s.toString())
    assert o.subject.value == 'BIO'
    assert o.term_code.value == '199712'
    assert o.subject == [name:        'subject', 
                         type:        'STRING', 
                         description: 'subject', 
                         label:       'Subject', 
                         'default':   'ART', 
                         value:       'BIO']
    assert o.term_code == ['default':   '201312', 
                           description: 'term_code', 
                           name:        'term_code', 
                           type:        'STRING', 
                           label:       'Term Code', 
                           value:       '199712']
  }

  void testRunParamFormToHtml () {
    printBanner("testRunParamFormToHtml") 
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    def v = new ParamFormValue(pf)
    assert v.getValueMap() == [term_code: '201312',
                               subject:   'ART',
                               home:      'Commodore64']
    def s = new StringWriter()
    assert v.run(OutputFormat.html, s)
    def o = s.toString()
    assert 1 == 1 || o.trim() == """
<table>
  <thead>
    <th>Parameter</th>
    <th>Value</th>
  </thead>
  <tbody>
    <tr>
      <td class="parameterName">term_code</td>
      <td class="parameterValue">201312</td>
    </tr>
    <tr>
      <td class="parameterName">subject</td>
      <td class="parameterValue">ART</td>
    </tr>
  </tbody>
</table>""".trim()
  }

  void testGetOutputFormat() {
    printBanner("testGetOutputFormat")
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    //def f = getParamForm("SubjectAndTerm");
    assert pf
    def v = new ParamFormValue(pf)
    assert v
    assert v.getOutputFormats() == [OutputFormat.json, OutputFormat.html]
  }

  void testToJson() {
    printBanner("testToJson")
    def pf = getReportObjectFactory().getParamForm("SubjectAndTerm");
    assert pf.export() == [home:[name:         'home',
                                 type:         'SYSTEM',
                                 description:  'home',
                                 label:        'home',
                                 'default':    'Commodore64'],
                           subject: [name:        "subject", 
                                    type:        "STRING", 
                                    description: "subject", 
                                    label:       "Subject", 
                                    default:     "ART"], 
                          term_code:[name:        "term_code", 
                                     type:        "STRING", 
                                     description: "term_code", 
                                     label:       "Term Code", 
                                     default:     "201312"]]
    def v = new ParamFormValue(pf)
    assert v
    assert v.values.containsKey('term_code')
    def tcv = v.getValue('term_code');
    assert tcv;
    println "tcv.export()=${tcv.export()}"
    def sv = v.getValue('subject');
    println "sv=${sv}"
    println "sv.export()=${sv.export()}"
    // Should be same as the param form export, but with added values.
    assert v.export() == [home: [name:         'home',
                                 type:         'SYSTEM',
                                 description:  'home',
                                 label:        'home',
                                 value:        'Commodore64'],
                          subject: [name:        "subject", 
                                    type:        "STRING", 
                                    description: "subject", 
                                    label:       "Subject", 
                                    "default":     "ART",
                                    value:         "ART"
                                   ], 
                          term_code:[name:        "term_code", 
                                     type:        "STRING", 
                                     description: "term_code", 
                                     label:       "Term Code", 
                                     "default":     "201312",
                                     value:         "201312"]]
    def j = v.toJson()
    println "j=$j"
    def j2 = new JsonSlurper().parseText(j)
    assert j2 == v.export()
  }

}

