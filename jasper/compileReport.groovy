@GrabResolver(name='Jasper', root='http://jaspersoft.artifactoryonline.com/jaspersoft/third-party-ce-artifacts/')
@Grapes([
  @Grab(group='net.sf.jasperreports', 
        module='jasperreports', 
        version='6.2.0'),
  // We need Rhino for reports that might use Javascript.
  @Grab(group='org.mozilla', module='rhino', version='1.7.7')
])

import net.sf.jasperreports.engine.*;
args.each {
  arg ->
    JasperCompileManager.compileReportToFile(arg);
}