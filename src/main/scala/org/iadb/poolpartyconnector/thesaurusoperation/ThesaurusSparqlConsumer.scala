package org.iadb.poolpartyconnector.thesaurusoperation


import java.net.URL

import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification.LanguageLiteral

import scala.util.Try

//import com.hp.hpl.jena.Jena
//import org.w3.banana.jena.{JenaModule}
import org.w3.banana._
import org.w3.banana.sesame.SesameModule


/**
 * Created by Daniel Maatari Okouya on 8/31/15.
 * TODO Make the code safe by ensuring that you return an empty list when there is nothing.
 */

trait ThesaurusSparqlConsumer extends RDFModule with RDFOpsModule with SparqlOpsModule with SparqlHttpModule  {



  import ops._
  import sparqlOps._
  import sparqlHttp.sparqlEngineSyntax._


  def getEca(EndpointUri: String, ConceptUri: String): List[String] = {

    val endpoint = new URL(EndpointUri)

    val query    = parseSelect(s"""
                            PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
                            SELECT DISTINCT ?webtopic
                            where {
                              {
                                <$ConceptUri> <http://thesaurus.iadb.org/idbdoc/idbdocmatch> ?webtopic
                              }
                              UNION
                              {
                                <$ConceptUri> skos:broaderTransitive ?concept .
                                ?concept skos:topConceptOf <http://thesaurus.iadb.org/publicthesauri/IdBTopics>.
                                ?concept <http://thesaurus.iadb.org/idbdoc/idbdocmatch> ?webtopic
                              }
                            } LIMIT 100
                            """).get

    //TODO Try over Parselect, and log problems
    val answersTry: Try[Rdf#Solutions] = endpoint.executeSelect(query)

    val topicsTry: Try[Iterator[Rdf#Literal]]  = answersTry map { answers => answers.iterator map { row => row("webtopic").get.as[Rdf#Literal].get } }

    topicsTry map {topics => topics.toList map {e => e.lexicalForm} } getOrElse List.empty

  }

  def isBroaderConcept(EndpointUri: String, maybeBroaderUri: String,  ConceptUri: String): Boolean = {

    val endpoint = new URL(EndpointUri)

    val query    = parseAsk(s"""
                            PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
                            ASK
                            {
                              <$ConceptUri> skos:broader+  <$maybeBroaderUri> .
                            }
                            """).get


    //TODO Try over Parselect, and log problems
    endpoint.executeAsk(query).getOrElse(false)

  }


  def getConceptIndexableLabels(EndpointUri: String, ConceptUri: String): List[LanguageLiteral] = {

    val endpoint = new URL(EndpointUri)

    val query    = parseSelect(s"""
                            PREFIX skos:<http://www.w3.org/2004/02/skos/core#>
                                  select (<$ConceptUri> As ?concept) ?indexableLabel
                                  {

                                    {
                                        <$ConceptUri> skos:prefLabel ?indexableLabel .
                                    }
                                    UNION
                                    {
                                        <$ConceptUri> skos:altLabel ?indexableLabel .
                                    }
                                    UNION
                                    {
                                        <$ConceptUri> skos:hiddenLabel ?indexableLabel .
                                    }
                                    UNION
                                    {
                                        <$ConceptUri> skos:broader+ ?broader .
                                        ?broader skos:prefLabel ?indexableLabel .
                                    }

                                  }  LIMIT 100
                            """).get

    /*val answers: Rdf#Solutions           = endpoint.executeSelect(query).get

    val languages: Iterator[Rdf#Literal] = answers.iterator map { row => row("indexableLabel").get.as[Rdf#Literal].get }

    languages.toList map {e => LanguageLiteral(e.lexicalForm, e.lang.get.toString)}*/

    val answersTry: Try[Rdf#Solutions]        = endpoint.executeSelect(query)

    val labelsTry: Try[Iterator[Rdf#Literal]] = answersTry map { answers => answers.iterator map { row => row("indexableLabel").get.as[Rdf#Literal].get }}

    val languageLiteralTry = labelsTry map {languages => languages.toList map {e => LanguageLiteral(e.lexicalForm, e.lang.get.toString)}}

    languageLiteralTry getOrElse List.empty

  }


  def getConceptAllLangPrefLabels(endpointUri: String, conceptUri: String): List[LanguageLiteral] = {

    val endpoint = new URL(endpointUri)

    val query    = parseSelect(s"""
                            PREFIX skos:<http://www.w3.org/2004/02/skos/core#>
                                  select (<$conceptUri> As ?concept) ?label
                                  {
                                    <$conceptUri> skos:prefLabel ?label .

                                  }  LIMIT 100
                            """).get


    val answersTry: Try[Rdf#Solutions]        = endpoint.executeSelect(query)

    val labelsTry: Try[Iterator[Rdf#Literal]] = answersTry map { answers => answers.iterator map { row => row("label").get.as[Rdf#Literal].get }}

    val languageLiteralTry = labelsTry map {languages => languages.toList map {e => LanguageLiteral(e.lexicalForm, e.lang.get.toString)}}

    languageLiteralTry getOrElse List.empty

  }


}


case class ThesaurusSparqlConsumerJenaImpl() extends ThesaurusSparqlConsumer with SesameModule {

}
