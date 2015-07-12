package org.iadb.poolpartyconnector.scalajavaconversion

import scala.collection.JavaConverters._


/**
 * Created by Daniel Maatari Okouya on 7/10/15.
 *
 * Encapsulation of conversation of Scala class to Java
 */

object ScalatoJava {


  def  asJava[A,B](map: Map[A,B]): java.util.Map[A,B] = { map.asJava }

  def  asJava[A](list: List[A]): java.util.Collection[A] = {list.asJavaCollection}

}
