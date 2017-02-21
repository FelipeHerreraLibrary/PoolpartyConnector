package org.iadb.poolpartyconnector.thesaurusoperation

/**
  * Created by Daniel Maatari Okouya on 7/28/16.
  */
object ThesaurusSparqlConsumerTestApp extends App {

   /*
  println(ThesaurusSparqlConsumerJenaImpl().isBroaderConcept("http://thesaurus.iadb.org/PoolParty/sparql/publicthesauri",
                                                      "http://thesaurus.iadb.org/publicthesauri/5359760942722238",
                                                      "http://thesaurus.iadb.org/publicthesauri/208"))

  ThesaurusSparqlConsumerJenaImpl().getHistoryChangeSet(
                                                         "http://thesaurus.iadb.org/PoolParty/sparql/publicthesauri",
                                                         "2016-07-21T00:00:00Z"
                                                       ) foreach  {
    println(_)



  println(ThesaurusSparqlConsumerJenaImpl().isBroaderConcept("http://localhost:8086/PoolParty/sparql/publicthesauri",
    "http://localhost:8086/publicthesauri/5359760942722238",
    "http://localhost:8086/publicthesauri/208"))

  ThesaurusSparqlConsumerJenaImpl().getHistoryChangeSet(
    "http://localhost:8086/PoolParty/sparql/publicthesauri",
    "2016-07-21T00:00:00Z"
  ) foreach  {
    println(_)

  println(ThesaurusSparqlConsumerJenaImpl().getAllNarrowerIds("http://localhost:8086/PoolParty/sparql/publicthesauri",
    "http://thesaurus.iadb.org/publicthesauri/132913920474444178771096") )


  println(ThesaurusSparqlConsumerJenaImpl().getLastModifiedDate("http://localhost:8086/PoolParty/sparql/publicthesauri",
    "http://thesaurus.iadb.org/publicthesauri/45466564926317124") )

  println(ThesaurusSparqlConsumerJenaImpl().getCreatedDate("http://localhost:8086/PoolParty/sparql/publicthesauri",
    "http://thesaurus.iadb.org/publicthesauri/45466564926317124") )

  println(ThesaurusSparqlConsumerJenaImpl().getAllNarrowerIds("http://localhost:8086/PoolParty/sparql/publicthesauri",
    "http://thesaurus.iadb.org/publicthesauri/45466564926317124"))

  println(ThesaurusSparqlConsumerJenaImpl().getBroaderId("http://localhost:8086/PoolParty/sparql/publicthesauri",
    "http://thesaurus.iadb.org/publicthesauri/45466564926317124"))

  println(ThesaurusSparqlConsumerJenaImpl().getBroaderId("http://localhost:8086/PoolParty/sparql/publicthesauri",
    "http://thesaurus.iadb.org/publicthesauri/132913920474444178771096"))


   println(ThesaurusSparqlConsumerJenaImpl().getConceptUriByCode("http://localhost:8086/PoolParty/sparql/publicthesauri", "GE-GES"));

  println(ThesaurusSparqlConsumerJenaImpl().getConceptUriByCode("http://localhost:8086/PoolParty/sparql/publicthesauri", "ITE"));

   println(ThesaurusSparqlConsumerJenaImpl().getConceptUriByCode("http://localhost:8086/PoolParty/sparql/publicthesauri", "GE-G"));

  println(ThesaurusSparqlConsumerJenaImpl().getShemaFromCode("http://localhost:8086/PoolParty/sparql/publicthesauri", "GE-GES"));

  println(ThesaurusSparqlConsumerJenaImpl().getShemaFromCode("http://localhost:8086/PoolParty/sparql/publicthesauri", "GE-G"));


  println(ThesaurusSparqlConsumerJenaImpl().getMatchesConceptInSchema("http://localhost:8086/PoolParty/sparql/publicthesauri", "know", "http://thesaurus.iadb.org/publicthesauri/IdBTopics", 0, 5))

  println(ThesaurusSparqlConsumerJenaImpl().getMatchesConceptInSchema("http://localhost:8086/PoolParty/sparql/publicthesauri", "know", "http://thesaurus.iadb.org/publicthesauri/IdBDepartments",0,5))

  println(ThesaurusSparqlConsumerJenaImpl().getMatchesConceptInSchema("http://localhost:8086/PoolParty/sparql/publicthesauri", "know", "http://thesaurus.iadb.org/publicthesauri/IdBDepartments",1,5))

  println(ThesaurusSparqlConsumerJenaImpl().getMatchesConceptInSchema("http://localhost:8086/PoolParty/sparql/publicthesauri", "know", "http://thesaurus.iadb.org/publicthesauri/IdBDepartments",2,5))

  */
  
  println(ThesaurusSparqlConsumerJenaImpl().getIdentifier("http://localhost:8086/PoolParty/sparql/publicthesauri", "2010-01-01", "2016-12-31", 0, 10))

}
