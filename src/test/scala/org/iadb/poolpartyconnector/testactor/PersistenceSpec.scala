package org.iadb.poolpartyconnector.testactor

//<start id="persistence-persistence_spec"/>

import java.io.File

import com.typesafe.config._

import scala.util._
import akka.actor._
import akka.persistence._
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest._
import org.apache.commons.io.FileUtils

abstract class PersistenceSpec(system: ActorSystem) extends TestKit(system)
  with ImplicitSender
  with FeatureSpecLike
  with Matchers
  with GivenWhenThen
  with BeforeAndAfterAll
  with BeforeAndAfterEach
  with PersistenceCleanup {

  def this(name: String, config: Config) = this(ActorSystem(name, config))

  override protected def beforeAll()     = deleteStorageLocations()

  override protected def afterAll() = {
    deleteStorageLocations()
    TestKit.shutdownActorSystem(system)
  }


  override protected def beforeEach() : Unit = {
    deleteStorageLocations()
    super.beforeEach()
  }

  override protected def afterEach(): Unit = {
    super.afterEach()
    deleteStorageLocations()
  }

  def killActors(actors: ActorRef*) = {
    actors.foreach { actor =>
      watch(actor)
      system.stop(actor)
      expectTerminated(actor)
    }
  }
}

trait PersistenceCleanup {
  def system: ActorSystem

  val storageLocations = List(
    "akka.persistence.journal.leveldb.dir",
    "akka.persistence.journal.leveldb-shared.store.dir",
    "akka.persistence.snapshot-store.local.dir").map { s =>
    new File(system.settings.config.getString(s))
  }

  def deleteStorageLocations(): Unit = {
    storageLocations.foreach{
      dir => /*Try(*/FileUtils.deleteDirectory(dir)/*)*/ /*match {
        case Success(e) => system.log.debug(s"Deleting: ${dir.getName} was a success: ${e} ")
        case Failure(e) => system.log.debug(s"Deleting: ${dir.getName} was a failure: ${e} ")
      }*/
    }
  }
}
//<end id="persistence-persistence_spec"/>
