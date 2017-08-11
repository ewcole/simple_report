/* Produce a report from an arbitrary Groovy script. */
report() {
  data_generator closure: {
    column(name: "num", label: "Number")
    column(name: "square", label: "Square")
    column(name: "cube")
    (1..5).each {
      row(num: it, square: it*it, cube: it*it*it)
    }
  }
}
