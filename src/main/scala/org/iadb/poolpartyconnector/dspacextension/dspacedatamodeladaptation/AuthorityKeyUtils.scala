package org.iadb.poolpartyconnector.dspacextension.dspacedatamodeladaptation

/**
 * Created by Daniel Maatari Okouya on 6/22/15.
 */

case class AuthorityandKey(auth: String, key:String)

object AuthorityKeyUtils {

  //TODO Test and handle corner case to return ("", "")
  def splitAuthrotiyandKey(authkey: String): AuthorityandKey = {
    val authkeyTuple = authkey.splitAt(authkey.indexOf(':', 5))
    AuthorityandKey(authkeyTuple._1, authkeyTuple._2.substring(1))
  }

}
