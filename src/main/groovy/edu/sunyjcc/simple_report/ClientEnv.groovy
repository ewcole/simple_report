package edu.sunyjcc.simple_report

/** An interface through which the client application can provide methods 
 *  and properties to the Simple Reports system
 */
public class ClientEnv extends Expando {
  SystemParams systemParams = new SystemParams()

  SystemParams getSystemParams() {
    if (!systemParams) {
      this.systemParams = new SystemParams()
    }
    this.systemParams
  }
  
}
