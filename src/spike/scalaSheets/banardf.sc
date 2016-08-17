import java.time._
import java.time.temporal.ChronoUnit

import org.iadb.poolpartyconnector.thesaurusoperation.ThesaurusSparqlConsumerJenaImpl


val y = (x: Int) => { x * 2 }


/*val x = 1
 x match {

   case _ => x
   case 1 => "It's one"
 }*/

/*
Instant.now(Clock.system(ZoneId.of("America/Los_Angeles"))).truncatedTo(ChronoUnit.SECONDS)
*/

//ZonedDateTime.now().format()

//Instant.parse("2016-08-02T18:33:21Z").plusSeconds(3)

/*ZoneId.getAvailableZoneIds

ZoneId.of("-05:00")*/
//println(ThesaurusSparqlConsumerJenaImpl().getEca("http://127.0.0.1:8086/PoolParty/sparql/publicthesauri", "http://thesaurus.iadb.org/publicthesauri/82451829792987269"))

/*ThesaurusSparqlConsumerJenaImpl().
  getConceptIndexableLabels("http://127.0.0.1:8086/PoolParty/sparql/publicthesauri", "http://thesaurus.iadb.org/publicthesauri/35199195070012766377107").
  foreach(e => println(s"${e.label}@${e.language}"))*/
/*val atry : Try[Int] = Failure(new Throwable("failed"))
val atry2: Try[Int] = atry.map(e => e + 2)*/

/*

"hello"
println(ThesaurusSparqlConsumerJenaImpl().isBroaderConcept(
  "http://thesaurus.iadb.org/PoolParty/sparql/publicthesauri",
  "http://thesaurus.iadb.org/publicthesauri/5359760942722238",
  "http://thesaurus.iadb.org/publicthesauri/208"))*/



/*ThesaurusSparqlConsumerJenaImpl().getHistoryChangeSet(
                                                       "http://thesaurus.iadb.org/PoolParty/sparql/publicthesauri",
                                                       "2016-07-21T00:00:00Z"
                                                     ) foreach  {
  e => println(s"${e.change.toString} : ${e.changeInstant.toString}")
}*/


/*
val func = (x:List[Int], y:Int) => {

  x.exists( e => e == y) match {
    case false => y::x
    case true => x
  }

}

val list = List (1, 2, 2, 4, 5, 13, 4, 5, 6, 7, 8, 9, 10, 11, 13, 5, 19)

(list.foldLeft (List.empty[Int]) {func}).reverse


list.foldRight(List.empty[Int]){ (e:Int, f:List[Int]) =>

  f.exists(_ == e) match {
    case false => e::f
    case _ => f
  }

}
*/


(1 to 10).foldRight("")((myint, myString) => myString + myint)