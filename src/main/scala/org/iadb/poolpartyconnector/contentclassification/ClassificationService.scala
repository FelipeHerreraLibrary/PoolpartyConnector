package org.iadb.poolpartyconnector.contentclassification

import java.io.InputStream

import org.iadb.poolpartyconnector.utils.JsonUtils.ConceptResults

/**
 * Created by Daniel Maatari Okouya on 6/6/15.
 */
trait ClassificationService {

  def recommendMetadata(inputstream: InputStream) : ConceptResults

}
