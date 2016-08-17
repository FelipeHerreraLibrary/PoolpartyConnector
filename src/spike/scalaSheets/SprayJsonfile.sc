import spray.json._

import scala.io.Source


val tmdb = Source.fromFile("/Users/maatary/Dev/data/Elastic/tmdb.json").mkString.parseJson

tmdb.prettyPrint


s"""PREFIX skos:<http://www.w3.org/2004/02/skos/core#>
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

      FILTER (?createdDate >= "2016-07-21T00:00:00Z"^^xsd:dateTime )
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
    Filter Exists {graph <http://schema.semantic-web.at/ppt/history#context> { ?event change:changeType "REMOVAL" } }
  }

} order by desc(?createdDate)"""
