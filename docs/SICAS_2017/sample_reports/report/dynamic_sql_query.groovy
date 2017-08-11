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
  dynamic_sql sql_closure: {
    params ->
      // Use the selected grade column
      String mid_or_final = params.mid_or_final;
      String grade = params.grade;
      String grade_column = (mid_or_final == 'M')?'mid_grade':'final_grade'
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
