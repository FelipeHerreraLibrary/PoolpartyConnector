import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

val unfilteredList: List[Int] = 1 to 20 toList

val futureRes = Future.sequence(
  List(Future{unfilteredList.withFilter(e => (e % 4) == 0).flatMap(e => List(e))},
       Future{unfilteredList.withFilter(e => (e % 7) == 0).flatMap(e => List(e))}))

Await.result(futureRes, Duration.Inf).flatten

for (i <- 1 to 10) yield (if ( i == 3) i else 2)