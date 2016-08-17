package org.iadb.poolpartyconnector.thesaurusoperation

/**
  * Created by Daniel Maatari Okouya on 7/22/16.
  */
trait ThesaurusSparqlConsumerModule {

  import com.softwaremill.macwire._


  val thesaurusSparqlConsumer: ThesaurusSparqlConsumer = wire[ThesaurusSparqlConsumerJenaImpl]

}


object ThesaurusSparqlConsumerModule extends ThesaurusSparqlConsumerModule