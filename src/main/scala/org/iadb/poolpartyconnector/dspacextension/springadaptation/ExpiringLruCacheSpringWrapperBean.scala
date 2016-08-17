package org.iadb.poolpartyconnector.dspacextension.springadaptation

import spray.caching.ExpiringLruCache

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.concurrent.duration._

/**
  * Created by Daniel Maatari Okouya on 8/10/16.
  */
trait ExpiringLruCacheSpringWrapperBean {val expiringLruCache: ExpiringLruCache[String]}

class ExpiringLruCacheSpringWrapperBeanImpl(maxCapacity     : Long,
                                            initialCapacity : Int,
                                            timeToLive      : Duration,
                                            timeToIdle      : Duration) extends ExpiringLruCacheSpringWrapperBean {

   println(s"I m the ExpiringLruCacheSpringWrapperBean numer: ${System.identityHashCode(this)}")

   val expiringLruCache: ExpiringLruCache[String] = new ExpiringLruCache[String](maxCapacity, initialCapacity, timeToLive, timeToIdle)



}
