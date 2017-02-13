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
  } */

  println(ThesaurusSparqlConsumerJenaImpl().isBroaderConcept("http://localhost:8086/PoolParty/sparql/publicthesauri",
    "http://localhost:8086/publicthesauri/5359760942722238",
    "http://localhost:8086/publicthesauri/208"))

  ThesaurusSparqlConsumerJenaImpl().getHistoryChangeSet(
    "http://localhost:8086/PoolParty/sparql/publicthesauri",
    "2016-07-21T00:00:00Z"
  ) foreach  {
    println(_)
  }
}
