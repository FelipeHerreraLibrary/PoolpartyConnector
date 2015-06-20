package org.iadb.poolpartyconnector.dspaceutils

import org.dspace.content.{WorkspaceItem, Collection, Item}
import org.dspace.core.Context
import org.scalatest.{FeatureSpec, Matchers, GivenWhenThen}

/**
 * Created by Daniel Maatari Okouya on 6/11/15.
 */
class createDspaceItemTest extends FeatureSpec with DspaceTestFixture with Matchers with GivenWhenThen {






  scenario("an item is created into dspace") {

    pending
    /*Given("the collection")
      context = new Context
      context.turnOffAuthorisationSystem;
      val cols = Collection.findAll(context)
      val col = cols.find(col => col.getHandle == "iadb/81").get

    When("an item is create in that collection")
      val wi = WorkspaceItem.create(context, col, true);

    Then("the item should not be archived")
    //we need to commit the changes so we don't block the table for testing
      wi.getItem.isArchived should be(false)

    context.abort()

    context.restoreAuthSystemState();
    println("done")*/

  }

}
