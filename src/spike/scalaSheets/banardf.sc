import org.iadb.poolpartyconnector.thesaurusoperation.ThesaurusSparqlConsumerJenaImpl

import scala.util.{Failure, Try}


//println(ThesaurusSparqlConsumerJenaImpl().getEca("http://127.0.0.1:8086/PoolParty/sparql/publicthesauri", "http://thesaurus.iadb.org/publicthesauri/82451829792987269"))

/*ThesaurusSparqlConsumerJenaImpl().
  getConceptIndexableLabels("http://127.0.0.1:8086/PoolParty/sparql/publicthesauri", "http://thesaurus.iadb.org/publicthesauri/35199195070012766377107").
  foreach(e => println(s"${e.label}@${e.language}"))*/
/*val atry : Try[Int] = Failure(new Throwable("failed"))
val atry2: Try[Int] = atry.map(e => e + 2)*/

ThesaurusSparqlConsumerJenaImpl().isBroaderConcept(
  "http://127.0.0.1:8086/PoolParty/sparql/publicthesauri",
  "http://thesaurus.iadb.org/publicthesauri/149109721920228247869246",
  "http://thesaurus.iadb.org/publicthesauri/65681465830111029997471")