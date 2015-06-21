package org.iadb.poolpartyconnector.dspaceutils

import org.dspace.content.Item
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, Matchers, FeatureSpec}

/**
 * Created by Daniel Maatari Okouya on 6/6/15.
 */
class AddThesaurusConceptsAsItemMetadataSpec extends FeatureSpec with MockFactory with Matchers with GivenWhenThen {


    scenario("A non-empty set of metadata and an Item are submitted for an item metadata addation"){


      Given("a set of recommended metadata, a newly created Item and the RecommendedMetadataUtils")

        val item = mock[DspaceItemWrapper]

      Then("the item addMetadata should be called once with (\"hi\", \"he\", \"hey\", \"test\", \"holla\", \"hey\", 11) as args ")


        (item.addMetadata(_: String, _:String, _:String, _:String, _:String, _:String, _:Int)) expects ("hi", "he", "hey", "test", "holla", "hey", 11)


      When("requesting the RecommmendedMetadataUtils to provide for the item updated with the recommendedMetadata (\"hi\", \"he\", \"hey\", \"test\", \"holla\", \"hey\", 10)")

         item.addMetadata("hi", "he", "hey", "test", "holla", "hey", 11)

        //RecommmendedMetadataUtils.deliverItemwithRecommendedMetadata(item, recommendedmetadata)






  }


}
