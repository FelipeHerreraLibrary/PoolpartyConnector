package org.iadb.poolpartyconnector.utils

import java.io.File
import java.nio.file.Files

import org.scalatest.{FunSpec, FeatureSpec, GivenWhenThen, Matchers}

/**
 * Created by Daniel Maatari Okouya on 6/5/15.
 */
class CopytoTemporaryFileUnitTest extends FeatureSpec with Matchers with GivenWhenThen {


  scenario("Copying File") {


    Given("a file opened as an Inputstream and a temporary file")

      val file = new File(getClass.getResource("/UNWOMEN_surveyreport_ADVANCE_16Oct-short.pdf").toURI)
      val inputstream = Files.newInputStream(file.toPath)

    When("Requesting a temporary copy of that file to the temporaryCopyService")

      val tmpToClassify = TemporaryCopyUtils.getemporaryCopy(inputstream)

    Then("the temporary file should have the same size on disk as the original inputstream file")

      tmpToClassify.getTotalSpace should be (file.getTotalSpace)

  }



}
