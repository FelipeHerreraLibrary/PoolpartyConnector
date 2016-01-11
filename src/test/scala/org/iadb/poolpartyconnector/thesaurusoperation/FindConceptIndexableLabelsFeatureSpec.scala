package org.iadb.poolpartyconnector.thesaurusoperation

import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification.LanguageLiteral
import org.scalatest.{GivenWhenThen, Matchers, FeatureSpec}

/**
  * Created by Daniel Maatari Okouya on 11/9/15.
  */
class FindConceptIndexableLabelsFeatureSpec extends FeatureSpec with ThesaurusConsumerServiceFixture with Matchers with GivenWhenThen {



  feature("Resolution of a Concept Indexable labels i.e. including, hidden, broader, etc...") {


    info("As a Client of the ThesarusComsumer Service, I want to be able to resolves all the indexable labels of a concept, to power my search functionality")


    scenario("The URI of an Existing Concept is submitted to the ThesarusConsumer for Resolution "){


      Given("The URI of an Existing Concept ")

        val conceptUri = "http://thesaurus.iadb.org/publicthesauri/90342360546305963776674"

      When("")

        val indexablelabels = cache.getIndexableLabels(conceptUri)

      Then("")

        indexablelabels should contain allOf (
                LanguageLiteral("Child labor","en"),
                LanguageLiteral("Trabajo infantil", "es"),
                LanguageLiteral("Child labour", "en"),
                LanguageLiteral("Labor", "en"),
                LanguageLiteral("Empleo", "es"),
                LanguageLiteral("Emprego", "pt")/*,
                LanguageLiteral("Habilidades cognitivas", "pt"),
                LanguageLiteral("compétences cognitives", "fr"),
                LanguageLiteral("Child development", "en"),
                LanguageLiteral("Desarrollo infantil", "es"),
                LanguageLiteral("Desenvolvimento infantil", "pt"),
                LanguageLiteral("Health", "en"),
                LanguageLiteral("Salud", "es"),
                LanguageLiteral("Saúde", "pt"),
                LanguageLiteral("Jóvenes y niños", "es"),
                LanguageLiteral("Youth & Children", "en"),
                LanguageLiteral("Desarrollo social", "es"),
                LanguageLiteral("Desenvolvimento Social", "pt"),
                LanguageLiteral("Développement social", "fr"),
                LanguageLiteral("Social Development", "en")*/
          )

    }

  }

}
