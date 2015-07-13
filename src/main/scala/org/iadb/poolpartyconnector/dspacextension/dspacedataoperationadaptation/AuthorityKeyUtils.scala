package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation

import org.iadb.poolpartyconnector.dspacextension.dspacedatamodeladaptation.AuthorityandKey


object AuthorityKeyUtils {

  //TODO Test and handle corner case to return ("", "")
  def splitAuthrotiyandKey(authkey: String): AuthorityandKey = {
    val authkeyTuple = authkey.splitAt(authkey.indexOf(':', 5))
    AuthorityandKey(authkeyTuple._1, authkeyTuple._2.substring(1))
  }

}
