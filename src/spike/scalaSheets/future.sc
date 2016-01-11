import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.duration._
import scala.concurrent._


val value = for (j <- Future {println("hello"); 2} ) yield (j + 1)

value

value.value

Future {2}.flatMap(e => Future{e + 1}).onComplete {case e => println(e)}



val f = Future { 5 }

val h = f filter { _ % 2 == 0 } map {e => e + 20 } recoverWith {case _ => Future{10}}

//val g = f filter  { _ % 2 == 1 } recoverWith {case _ => Future{10}}

//Await.result(g, 1 second ) // evaluates to 5
Await.result(h, 1 second )


val failed = Future.failed[Int](new Throwable("Myfailure")).withFilter((e:Int) => e % 2 == 0).recoverWith {case e:Throwable => Future.successful(e.getMessage)}


Await.result(failed, 1 second )