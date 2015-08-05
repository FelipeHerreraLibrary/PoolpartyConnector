package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.consumeroperation

import org.dspace.content.DCValue
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.DspaceDcValueUtils

/**
 * Created by Daniel Maatari Okouya on 8/4/15.
 */
object ConsumerUtil {



  def copyDCValuesWithSuggestionsUris(dcValues: List[DCValue], Uris: List[String]) : List[DCValue] = {

    dcValues.zip(Uris).map { e =>
      DspaceDcValueUtils.getDCValue(e._1.element, e._1.qualifier, e._2, e._1.language, e._1.schema, e._2, e._1.confidence)
    }

  }


}
