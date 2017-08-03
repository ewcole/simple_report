report(title: "Grade Totals") {
  param(name: 'mid_or_final',
        label: "Show Midterm or Final Grade?",
        'default': 'F') {
    list_of_values values: [['F', 'Final'],
                            ['M', 'Midterm']]
  }

  param(name: 'grade',
        label: 'What grade do we report on?',
        'default': 'F') {
    list_of_values values: ('A'..'F').collect{[it, it]}
  }

  // The data source for this report is a dynamic SQL query.
  //     The dynamic_sql method takes a closure as an argument.
  //     This one will use the value of the mid_or_final parameter
  //     to choose which grade column to report on, and it will
  //     also use the grade parameter to set one of the column names.
  dynamic_sql sql_closure: {
    params ->
      
      // Get the parameters from the closure arguments.
      String mid_or_final = params.mid_or_final;
      String grade = ('A'..'F').contains(params.grade)?params.grade:'F';
      
      // Set the grade column 
      String grade_column = (mid_or_final == 'M')?'mid_grade':'final_grade'

      // Return an SQL query.
      """
       select semester,
              count(${grade_column}) as ${grade_column},
              sum(decode(${grade_column}, :grade, 1, 0)
                 ) ${grade}_grades
           from summit_grades
           group by semester
           order by semester
      """
  }
}
