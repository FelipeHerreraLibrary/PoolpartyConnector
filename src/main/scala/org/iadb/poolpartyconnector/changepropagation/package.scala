package org.iadb.poolpartyconnector

import java.io.Serializable
import java.time.Instant

import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification.LanguageLiteral


/**
  * Created by Daniel Maatari Okouya on 7/25/16.
  */
package object changepropagation {




  sealed trait Change
  sealed trait AtomicChange  extends  Change {val subjectOfChangeURI: String}
  sealed trait Addition extends AtomicChange {val subjectOfChangeURI: String; val newValue: LanguageLiteral}
  sealed trait Update  extends AtomicChange {val subjectOfChangeURI: String; val newValue: LanguageLiteral; val oldValue: LanguageLiteral}
  sealed trait Merge extends Change {val subjectOfChangeURI: String; val mergingConceptURI: String}

  case class PrefAdded(subjectOfChangeURI: String, newValue: LanguageLiteral) extends Addition
  case class AltAdded(subjectOfChangeURI: String, newValue: LanguageLiteral) extends Addition
  case class HiddenAdded(subjectOfChangeURI: String, newValue: LanguageLiteral) extends Addition

  case class AltChanged(subjectOfChangeURI: String, newValue: LanguageLiteral, oldValue: LanguageLiteral) extends Update
  case class PrefChanged(subjectOfChangeURI: String, newValue: LanguageLiteral, oldValue: LanguageLiteral)extends Update
  case class HiddenChanged(subjectOfChangeURI: String, newValue: LanguageLiteral, oldValue: LanguageLiteral) extends Update

  case class ConceptMerged(subjectOfChangeURI: String, mergingConceptURI: String) extends Merge



  case class ChangeEvent(change: Change, changeInstant: Instant)


  sealed trait PropagationOp {val historyTime: Instant}
  case class ConceptUpdatePropagation(subjectOfChangeURI: String, historyTime: Instant) extends PropagationOp
  case class ConceptMergePropagation(subjectOfChangeURI: String, mergingConceptURI: String, historyTime: Instant) extends PropagationOp


  /**
    * Tag Representing a ChangeWriterActor and a ChangeFetcherActor
    * for dependency injection.
    */

  sealed trait Writer
  sealed trait Fetcher
  sealed trait EndPoint


}
