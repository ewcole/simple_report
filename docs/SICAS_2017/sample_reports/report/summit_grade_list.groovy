report(title: "Summit Grade List") {
  param(name: "semester", label: "semester") {
    list_of_values query: """select distinct semester 
                                 from summit_grades"""
  }
  sql query: """select * from summit_grades 
                    where semester = :semester"""
}
