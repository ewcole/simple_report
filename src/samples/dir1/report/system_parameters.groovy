report(name: 'system_parameters') {
  data_generator closure: {
    column(name: 'param_name');
    column(name: 'param_value');
    assert params;
    row(param_name: "params.size()", param_value: params.size());
    // row(param_name: "params", param_value: params);
    // row(param_hame: "params.getClass()", param_value: "${params.getClass()}")
    // row(param_hame: "params.keySet()", param_value: "${params.keySet()}")
    params.each {
      pName, pVal ->
      row(param_name: pName, param_value: pVal);
    }
  }
}
