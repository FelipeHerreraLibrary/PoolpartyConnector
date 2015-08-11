

  /* gets a SparqlEngine out of a Sparql endpoint */

  val endpoint = new URL("http://dbpedia.org/sparql/")

  /* creates a Sparql Select query */

  val query = parseSelect("""
PREFIX ont: <http://dbpedia.org/ontology/>
SELECT DISTINCT ?language WHERE {
 ?language a ont:ProgrammingLanguage .
 ?language ont:influencedBy ?other .
 ?other ont:influencedBy ?language .
} LIMIT 100
                          """).get

  /* executes the query */

  val answers: Rdf#Solutions = endpoint.executeSelect(query).getOrFail()

  /* iterate through the solutions */

  val languages: Iterator[Rdf#URI] = answers.iterator map { row =>
    /* row is an Rdf#Solution, we can get an Rdf#Node from the variable name */
    /* both the #Rdf#Node projection and the transformation to Rdf#URI can fail in the Try type, hense the flatMap */
    row("language").get.as[Rdf#URI].get
  }

  println(languages.to[List])
}
