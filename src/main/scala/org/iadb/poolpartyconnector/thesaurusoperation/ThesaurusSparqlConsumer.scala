package org.iadb.poolpartyconnector.thesaurusoperation


import java.net.URL
import java.time.Instant

import org.iadb.poolpartyconnector.changepropagation._
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



  def getRepecId(endpointUri: String, conceptUri: Any): List[String] = {

    val endpoint = new URL(endpointUri)

    val query    = parseSelect(s"""
                            PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
                            SELECT DISTINCT ?repecid
                            where {
                              <$conceptUri> <http://thesaurus.iadb.org/repec/repecid> ?repecid
                            } LIMIT 1
                            """)

    //TODO Try over Parselect, and log problems
    val answersTry: Try[Rdf#Solutions] = query flatMap  { q => endpoint.executeSelect(q) }

    val topicsTry: Try[Iterator[Rdf#Literal]]  = answersTry map { answers => answers.iterator map { row => row("repecid").get.as[Rdf#Literal].get } }

    topicsTry map {topics => topics.toList map {e => e.lexicalForm} } getOrElse List.empty

  }


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
                                <$ConceptUri> skos:broader+ ?concept .
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


  def getAllLangBroaderLabels(EndpointUri: String, ConceptUri: String): List[LanguageLiteral] = {

    val endpoint = new URL(EndpointUri)

    val query    = parseSelect(s"""
                            PREFIX skos:<http://www.w3.org/2004/02/skos/core#>
                                  select ?indexableLabel
                                  {
                                    <$ConceptUri> skos:broader+ ?broader .

                                    {
                                      ?broader  skos:altLabel ?indexableLabel .
                                    }
                                    UNION
                                    {
                                      ?broader  skos:hiddenLabel ?indexableLabel .
                                    }
                                    UNION
                                    {
                                      ?broader skos:prefLabel ?indexableLabel .
                                    }

                                  }  LIMIT 100
                            """).get



    val answersTry: Try[Rdf#Solutions]        = endpoint.executeSelect(query)

    val labelsTry: Try[Iterator[Rdf#Literal]] = answersTry map { answers => answers.iterator map { row => row("indexableLabel").get.as[Rdf#Literal].get }}

    val languageLiteralTry = labelsTry map {languages => languages.toList map {e => LanguageLiteral(e.lexicalForm, e.lang.get.toString)}}

    languageLiteralTry getOrElse List.empty

  }

  def getAllLangNarrowerLabels(EndpointUri: String, ConceptUri: String): List[LanguageLiteral] = {

    val endpoint = new URL(EndpointUri)

    val query    = parseSelect(s"""
                            PREFIX skos:<http://www.w3.org/2004/02/skos/core#>
                                  select ?indexableLabel
                                  {
                                    <$ConceptUri> skos:narrower+ ?narrower .

                                    {
                                      ?narrower  skos:altLabel ?indexableLabel .
                                    }
                                    UNION
                                    {
                                      ?narrower  skos:hiddenLabel ?indexableLabel .
                                    }
                                    UNION
                                    {
                                      ?narrower skos:prefLabel ?indexableLabel .
                                    }

                                  }  LIMIT 100
                            """).get



    val answersTry: Try[Rdf#Solutions]        = endpoint.executeSelect(query)

    val labelsTry: Try[Iterator[Rdf#Literal]] = answersTry map { answers => answers.iterator map { row => row("indexableLabel").get.as[Rdf#Literal].get }}

    val languageLiteralTry = labelsTry map {languages => languages.toList map {e => LanguageLiteral(e.lexicalForm, e.lang.get.toString)}}

    languageLiteralTry getOrElse List.empty

  }

  def getAllLangRelatedLabels(EndpointUri: String, ConceptUri: String): List[LanguageLiteral] = {

    val endpoint = new URL(EndpointUri)

    val query    = parseSelect(s"""
                            PREFIX skos:<http://www.w3.org/2004/02/skos/core#>
                                  select ?indexableLabel
                                  {
                                    <$ConceptUri> skos:related ?related .

                                    {
                                      ?related  skos:altLabel ?indexableLabel .
                                    }
                                    UNION
                                    {
                                      ?related  skos:hiddenLabel ?indexableLabel .
                                    }
                                    UNION
                                    {
                                      ?related skos:prefLabel ?indexableLabel .
                                    }

                                  }  LIMIT 100
                            """).get



    val answersTry: Try[Rdf#Solutions]        = endpoint.executeSelect(query)

    val labelsTry: Try[Iterator[Rdf#Literal]] = answersTry map { answers => answers.iterator map { row => row("indexableLabel").get.as[Rdf#Literal].get }}

    val languageLiteralTry = labelsTry map {languages => languages.toList map {e => LanguageLiteral(e.lexicalForm, e.lang.get.toString)}}

    languageLiteralTry getOrElse List.empty

  }


  def getAllLangAltLabels(endpointUri: String, conceptUri: String): List[LanguageLiteral] = {

    val endpoint = new URL(endpointUri)

    val query    = parseSelect(s"""
                            PREFIX skos:<http://www.w3.org/2004/02/skos/core#>
                                  select ?label
                                  {
                                    <$conceptUri> skos:altLabel ?label .

                                  }  LIMIT 100
                            """).get


    val answersTry: Try[Rdf#Solutions]        = endpoint.executeSelect(query)

    val labelsTry: Try[Iterator[Rdf#Literal]] = answersTry map { answers => answers.iterator map { row => row("label").get.as[Rdf#Literal].get }}

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


  def getHistoryChangeSet(endpointUri: String, startDate: String): List[ChangeEvent] = {

    val endpoint = new URL(endpointUri)

    /*object queryexpression extends Enumeration {

    }*/

    val query    = parseSelect(s"""PREFIX skos:<http://www.w3.org/2004/02/skos/core#>
                                    PREFIX dcterms:<http://purl.org/dc/terms/>
                                    PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>
                                    PREFIX change:<http://purl.org/vocab/changeset/schema#>
                                    PREFIX history:<http://schema.semantic-web.at/ppt/history#>
                                    select distinct ?eventDescription ?createdDate ?changeType ?subjectOfChange ?newValue ?oldValue ?mergingConcept
                                    where {
                                        GRAPH <http://schema.semantic-web.at/ppt/history#context> {
                                          ?event change:changeType ?changeType .
                                          ?event change:createdDate ?createdDate .
                                          ?event change:creatorName ?creatorName .
                                          ?event change:subjectOfChange ?subjectOfChange .
                                          ?event history:changeOrder ?changeOrder .
                                          #filter(?changeOrder = 0) .
                                          ?event a ?type .
                                      	 optional { { ?event history:affectedLiteral ?newValue } UNION { ?event history:value ?newValue}}
                                          optional { ?event history:oldValue ?oldValue }
                                      	 optional { ?event history:predicate ?predicate }
                                          optional { ?event history:resourceType ?resourceType }
                                          #optional { ?event history:value ?value }

                                          FILTER (?createdDate > "${startDate}"^^xsd:dateTime )
                                          bind(
                                              if(?type = history:ResourceChangeHistoryEvent || ?type = history:MergeHistoryEvent,
                                                  # case history:ResourceChangeHistoryEvent
                                                  if(?type = history:ResourceChangeHistoryEvent,
                                                      if(?changeType = "ADDITION",
                                                          if(?resourceType = skos:ConceptScheme,"Concept Scheme added",
                                                              if(?resourceType = skos:Concept,"Concept added",
                                                              "")
                                                          ),
                                                      if(?changeType = "REMOVAL",
                                                          if(?resourceType = skos:ConceptScheme,"Concept Scheme deleted",
                                                              if(?resourceType = skos:Concept,"Concept deleted","")
                                                          ),
                                                      "")
                                                  ),
                                                  # case history:MergeHistoryEvent
                                                  if(?changeType = "ADDITION",
                                                     "Concept merge modified",
                                                     if(?changeType = "REMOVAL",
                                                        "Concept merge deprecated","")
                                                     )
                                                  ),
                                                  if(?type = history:LiteralAddRemoveHistoryEvent || ?type = history:LiteralUpdateHistoryEvent,
                                                      if(?changeType = "ADDITION",
                                                          if(?predicate = skos:prefLabel,"Preferred Label added",
                                                              if(?predicate = skos:altLabel,"Alternative Label added",
                                                                  if(?predicate = skos:hiddenLabel,"Hidden Label added",
                                                                  "")
                                                              )
                                                          ),
                                                      if(?changeType = "REMOVAL",
                                                          if(?predicate = skos:prefLabel,"Preferred Label deleted",
                                                              if(?predicate = skos:altLabel,"Alternative Label deleted",
                                                                  if(?predicate = skos:hiddenLabel,"Hidden Label deleted",
                                                                  "")
                                                              )
                                                          ),
                                                      if(?changeType = "UPDATE",
                                                          if(?predicate = skos:prefLabel,"Preferred Label changed",
                                                              if(?predicate = skos:altLabel,"Alternative Label changed",
                                                                  if(?predicate = skos:hiddenLabel,"Hidden Label changed",
                                                                  "")
                                                              )
                                                          ),
                                                      "")
                                                      )
                                                  ),
                                                  "")

                                              )
                                              as ?eventDescription)
                                    	filter(?eventDescription != "" && ?eventDescription != "Concept merge modified") .
                                        } .
                                      optional {
                                         ?subjectOfChange <http://purl.org/dc/terms/isReplacedBy> ?mergingConcept .
                                        #Filter Exists {graph <http://schema.semantic-web.at/ppt/history#context> { ?event change:changeType "REMOVAL" } }
                                      }

                                    } order by desc(?createdDate)""")

    val solutions = query.flatMap(e => endpoint.executeSelect(e))

    val changeList = solutions.map{solutions => solutions.iterator() flatMap  {row =>

      val eventDescription = row("eventDescription").get.as[Rdf#Literal].get
      val createdDate      = row("createdDate").get.as[Rdf#Literal].get
      val changeType       = row("changeType").get.as[Rdf#Literal].get
      val subjectOfChange  = row("subjectOfChange").get.as[Rdf#URI].get

      //TODO Change the unsafe get coming with them it is ridiculous
      val newValue         = row("newValue") map {e => Some(e.as[Rdf#Literal].get)} getOrElse(None)
      val oldValue         = row("oldValue") map {e => Some(e.as[Rdf#Literal].get)} getOrElse(None)
      val mergingConcept   = row("mergingConcept") map {e => Some(e.as[Rdf#URI].get)} getOrElse(None)




      changeType.lexicalForm match {
        case chgt if chgt == "ADDITION" => { //Requires only re-indexation
          eventDescription.lexicalForm match {
            case evtd if evtd == "Preferred Label added" =>
              Some(ChangeEvent(PrefAdded(subjectOfChange.getString, LanguageLiteral(newValue.get.lexicalForm, newValue.get.lang.get.toString)), Instant.parse(createdDate.lexicalForm)))
            case evtd if evtd == "Alternative Label added" =>
              Some(ChangeEvent(AltAdded(subjectOfChange.getString, LanguageLiteral(newValue.get.lexicalForm, newValue.get.lang.get.toString)), Instant.parse(createdDate.lexicalForm)))
            case evtd if evtd == "Hidden Label added" =>
              Some(ChangeEvent(HiddenAdded(subjectOfChange.getString, LanguageLiteral(newValue.get.lexicalForm, newValue.get.lang.get.toString)), Instant.parse(createdDate.lexicalForm)))
            case _ => None //TODO Concept Addition
          }

        }
        case chgt if chgt == "UPDATE" => { //Requires everything = Cache update + reindex
          eventDescription.lexicalForm match {
            case evtd if evtd == "Preferred Label changed" =>
              Some(ChangeEvent(PrefChanged(subjectOfChange.getString,
                                           LanguageLiteral(newValue.get.lexicalForm, newValue.get.lang.get.toString),
                                           LanguageLiteral(oldValue.get.lexicalForm, oldValue.get.lang.get.toString)),
                               Instant.parse(createdDate.lexicalForm)))
            case evtd if evtd == "Alternative Label changed" =>
              Some(ChangeEvent(AltChanged(subjectOfChange.getString,
                                           LanguageLiteral(newValue.get.lexicalForm, newValue.get.lang.get.toString),
                                           LanguageLiteral(oldValue.get.lexicalForm, oldValue.get.lang.get.toString)),
                               Instant.parse(createdDate.lexicalForm)))
            case evtd if evtd == "Hidden Label changed" =>
              Some(ChangeEvent(HiddenChanged(subjectOfChange.getString,
                                             LanguageLiteral(newValue.get.lexicalForm, newValue.get.lang.get.toString),
                                             LanguageLiteral(oldValue.get.lexicalForm, oldValue.get.lang.get.toString)),
                               Instant.parse(createdDate.lexicalForm)))
            case _ => None
          }
        }
        case chgt if chgt == "REMOVAL" => { //Requires everything = Cache update + reindex
          eventDescription.lexicalForm match {
            case evtd if evtd == "Concept merge deprecated" &&  mergingConcept != None =>
              Some(ChangeEvent(ConceptMerged(subjectOfChange.getString, mergingConcept.get.getString), Instant.parse(createdDate.lexicalForm)))
            case _ => None //TODO Concept Deleted or other Labels deleted
          }
        }
      }


    }}

    changeList map {e => e.toList} getOrElse(List.empty)

  }


  def getScheme(EndpointUri: String, ConceptUri: String): List[String] = {

    val endpoint = new URL(EndpointUri)

    val query    = parseSelect(s"""
                            PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
                            SELECT DISTINCT ?scheme
                            where {
                              {
                                <$ConceptUri> skos:inScheme ?scheme .
                              }
                              UNION
                              {
                                <$ConceptUri> skos:broader+ ?broader.
                                ?broader skos:inScheme ?scheme .
                              }
                            } LIMIT 100
                            """)


    val solutions: Try[Rdf#Solutions] = query.flatMap(q => endpoint.executeSelect(q))

    val literals = solutions.map { solutions => {solutions.iterator() map {row => row("scheme").get.as[Rdf#URI].get}}.toList }

    literals map {literals => literals map {e => e.getString} } getOrElse List.empty

  }

}


case class ThesaurusSparqlConsumerJenaImpl() extends ThesaurusSparqlConsumer with SesameModule {

}
