package org.iadb.poolpartyconnector.connectorconfiguration

/**
 * Created by Daniel Maatari Okouya on 7/9/15.
 */
case class PoolpartySettings(apirootEndpoint:String,
                             thesaurusapiEndpoint: String,
                             extratorapiEndpoint:String,
                             coreProjectId: String,
                             coreThesaurusUri: String,
                             jelProjectId: String,
                             jelThesaurusUri: String,
                             maxConceptsExtractionPool: Int,
                             maxTermsExtractionPool: Int,
                             coprusSettings: CorpusScoringSettings)
