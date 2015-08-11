import spray.caching._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

def lruCache[T](maxCapacity: Int = 500, initialCapacity: Int = 16,
                timeToLive: Duration = Duration.Inf, timeToIdle: Duration = Duration.Inf) =
  new ExpiringLruCache[T](maxCapacity, initialCapacity, timeToLive, timeToIdle)



val cache = lruCache[Int]()

/*

def fecthInt(k:String): Future[Int] = cache(k) {

  Future {

    //Thread.sleep(5000)
    println("fetching value")
    2
  }
}

Thread.sleep(2000)
fecthInt("me")

fecthInt("me").value*/

Await.result(cache("me")(Future{Thread.sleep(5000); 2}), Duration.Inf)




