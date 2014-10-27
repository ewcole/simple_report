package edu.sunyjcc.simple_report;

public class ParamFormValue implements Exportable {
  ParamForm paramForm;

  public ParamFormValue(ParamForm paramForm) {
    this.paramForm = paramForm
  }

  @Override
  public def export() {
    paramForm.export()
  }
}