import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import edu.sunyjcc.util.*

@GrabResolver(name='Jasper', root='http://jaspersoft.artifactoryonline.com/jaspersoft/third-party-ce-artifacts/')
@Grapes([
  @Grab(group='net.sf.jasperreports', module='jasperreports', version='6.2.0')
])

generateReport = {
  reportPath, outputFileName, params, connection ->
    try {
      // compile the report
      def reportFile = new File(reportPath);
      assert reportFile.exists();
      JasperReport jr = JasperCompileManager.compileReport(reportFile.newInputStream());
      // "Fill" the report, or put all the data into the report slots.
      JasperPrint jp = JasperFillManager.fillReport(jr, params, connection);
      JRDocxExporter export = new JRDocxExporter();
      export.setParameter(JRExporterParameter.JASPER_PRINT, jp);
      export.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFileName);
      export.exportReport()
    } catch (JRException ex) {
      ex.printStackTrace();
    }
}

def cli = new CliBuilder(usage: "Run a sample report");
cli.u(longOpt: 'userid', args: 1, required: true, "Oracle userid");
cli.r(longOpt: 'report_name', args: 1, required: true, "Name of report def file");
def opt = cli.parse(args);
if (!opt) {
  return;
}
def rf = new File(opt.r);
assert rf.exists()
def cm = new JccConnectionManager(opt.u);
assert cm.ora
 
generateReport(opt.r, "${opt.r}".replaceAll(/(\.jrxml)?$/, '.docx'), 
               [:], cm.ora.createConnection());