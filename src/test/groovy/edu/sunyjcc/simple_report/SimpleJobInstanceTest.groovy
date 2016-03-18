package edu.sunyjcc.simple_report

import groovy.util.BuilderSupport
import groovy.xml.*

/** Test the SimpleJobInstance class. */
public class SimpleJobInstanceTest extends GroovyTestCase {
  static void banner(String text) {
    println "${'*'*15} $text ${'*'*15}"
  }

  SimpleJob createJob() {
    SimpleJob job = new SimpleJob(name: "test_job",
                                  title: "Job to be used for testing");
    job.addParam(new Param(name: 'a', label: 'parameter a'));
    job.addParam(new Param(name: 'b', label: 'parameter b'));
    job.addParam(new Param(name: 'c', label: 'parameter c'));
    job.jobEngine = {
      markupBuilder.output {
        ('a'..'c').each {
          "$it"(value: "${params[it]}", "${params[it]}")
        }
      }
    }
    return job;
  }

  String sampleOutput() {
    def writeOutput = {
      mb, hsh ->
        mb.output {
          ('a'..'c').each {
            "$it"(value: "${hsh[it]}", "${hsh[it]}")
          }
        }
    }
    def s = new StringWriter()
    def mb = new MarkupBuilder(s)
    writeOutput(mb, "abc".inject([:]){m,v->m << ["$v": "*$v*"]})
    s.toString()
  }

  SimpleJobInstance createJobInstance() {
    new SimpleJobInstance(createJob())
  }

  void testCreateJob() {
    banner ('testCreateJob')
    // Make sure our sample job is OK.
    SimpleJob j = createJob()
    assert j.export() == [name: 'test_job',
                          type: 'SimpleJob',
                          title: 'Job to be used for testing',
                          version: '',
                          description: null,
                          params: [a: [name: 'a',
                                       type: 'STRING',
                                       description: null,
                                       label: 'parameter a',
                                       "default":null],
                                   b: [name: 'b',
                                       type: 'STRING',
                                       description: null,
                                       label: 'parameter b',
                                       "default": null],
                                   c: [name: 'c',
                                       type: 'STRING',
                                       description: null,
                                       label: 'parameter c',
                                       "default": null]]];
    def paramVals = ('a'..'c').inject([:]) {
      map, v ->
        map << ["$v": "*$v*"]
    }
    println "paramVals=$paramVals"
    def writeOutput = {
      mb, hsh ->
        mb.output {
          ('a'..'c').each {
            "$it"(value: "${hsh[it]}", "${hsh[it]}")
          }
        }
    }
    def s = new StringWriter()
    writeOutput(new MarkupBuilder(s), paramVals);
    def sw = new StringWriter();
    def m = new MarkupBuilder(sw);
    j.execute(('a'..'c').inject([:]) {
                map, v ->
                  map << ["$v": "*$v*"]
              }, m)
    assert sw.toString() == s.toString()
  }

  void testJobInstance() {
    banner "testJobInstance"
    def ji = createJobInstance()
    assert ji.getOutputFormats() == [OutputFormat.html]
    "abc".each {
      ji.params.setValue(it, "*$it*");
    }
    def s = new StringWriter();
    ji.run(OutputFormat.html, s)
    println s.toString()
    assert s.toString() == """<html>
  <head>
    <title>Job to be used for testing</title>
  </head>
  <body>
    <div class="simple_job test_job">
      <h1>Job to be used for testing</h1>
      <div class="job_output">
        <output>
          <a value="*a*">*a*</a>
          <b value="*b*">*b*</b>
          <c value="*c*">*c*</c>
        </output>
      </div>
    </div>
  </body>
</html>"""
  }
}
