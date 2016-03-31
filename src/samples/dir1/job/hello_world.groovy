job(name: 'hello_world') {
  param(name: 'whom', label: "Whom shall I greet?");
  jobEngine closure: {
    markupBuilder.div {
      String whom = params.whom?:"";
      params.each {
        name, value ->
          p("$name=$value");
      }
      if (whom.size()) {
        b("Hello, $whom!")
      } else {
        p("Hello, world.");
      }
    }
  }
}
