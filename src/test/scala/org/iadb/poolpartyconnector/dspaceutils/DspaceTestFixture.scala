package org.iadb.poolpartyconnector.dspaceutils

import java.io.{FileOutputStream, FileInputStream, IOException, File}
import java.net.URL
import java.nio.channels.FileChannel
import java.sql.SQLException
import java.util.{TimeZone, Properties}
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException

import org.apache.log4j.Logger
import org.dspace.administer.{RegistryImportException, MetadataImporter, RegistryLoader}
import org.dspace.authorize.AuthorizeException
import org.dspace.browse.{BrowseException, IndexBrowse}
import org.dspace.content.{NonUniqueMetadataException, MetadataField}
import org.dspace.core.{Context, ConfigurationManager}
import org.dspace.eperson.EPerson
import org.dspace.search.DSIndexer
import org.dspace.servicemanager.{DSpaceKernelInit, DSpaceKernelImpl}
import org.junit.Assert._
import org.scalatest.{BeforeAndAfterEach, Suite, BeforeAndAfterAll}

import scala.xml.SAXException

/**
 * Created by Daniel Maatari Okouya on 6/11/15.
 */
trait DspaceTestFixture extends BeforeAndAfterAll with BeforeAndAfterEach { this: Suite =>

  /** log4j category */
  private var log: Logger = Logger.getLogger(classOf[DspaceTestFixture])
  //Below there are static variables shared by all the instances of the class
  /**
   * Test properties
   */
  protected var testProps: Properties = null
  //Below there are variables used in each test
  /**
   * Context mock object to use in the tests
   */
  protected var context: Context = null
  /**
   * EPerson mock object to use in the tests
   */
  protected var eperson: EPerson = null

  /**
   * This method will be run before the first test as per @BeforeClass. It will
   * initialize resources required for the tests.
   *
   * Due to the way Maven works, unit tests can't be run from a POM package,
   * which forbids us to run the tests from the Assembly and Configuration
   * package. On the other hand we need a structure of folders to run the tests,
   * like "solr", "report", etc.
   *
   * This method will create all the folders and files required for the tests
   * in the test folder. To facilitate the work this will consist on a copy
   * from a folder in the resources folder. The ConfigurationManager will be
   * initialized to load the test "dspace.cfg" and then launch the unit tests.
   */
  /*@BeforeClass*/
  override def beforeAll(): Unit = { //BeforeAll

    super.beforeAll()

    try {
      TimeZone.setDefault(TimeZone.getTimeZone("Europe/Dublin"))
      testProps = new Properties
      val properties: URL = classOf[DspaceTestFixture].getClassLoader.getResource("test-config.properties")
      testProps.load(properties.openStream)
      val origin: URL = ClassLoader.getSystemResource("dspaceFolder")
      val source: File = new File(origin.getPath)
      val dspaceTmp: File = new File(testProps.getProperty("test.folder"))
      if (!dspaceTmp.exists) {
        dspaceTmp.mkdirs
      }
      copyDir(source, dspaceTmp)
      val tmpFile: File = new File(testProps.getProperty("test.bitstream"))
      val destParent: File = new File(testProps.getProperty("test.folder.assetstore"))
      destParent.mkdirs
      val dest: File = new File(testProps.getProperty("test.assetstore.bitstream"))
      if (!dest.exists) {
        dest.createNewFile
      }
      copyFile(tmpFile, dest)
      val configFile: URL = classOf[DspaceTestFixture].getClassLoader.getResource(testProps.getProperty("test.config.file"))
      ConfigurationManager.loadConfig(configFile.getPath)
      var kernelImpl: DSpaceKernelImpl = null
      try {
        kernelImpl = DSpaceKernelInit.getKernel(null)
        if (!kernelImpl.isRunning) {
          kernelImpl.start(ConfigurationManager.getProperty("dspace.dir"))
        }
      }
      catch {
        case e: Exception => {
          try {
            if (kernelImpl != null) {
              kernelImpl.destroy
            }
          }
          catch {
            case e1: Exception => {
            }
          }
          val message: String = "Failure during filter init: " + e.getMessage
          throw new IllegalStateException(message, e)
        }
      }
      var ctx: Context = new Context
      ctx.turnOffAuthorisationSystem

      if (MetadataField.find(ctx, 1) == null) {
        val base: String = testProps.getProperty("test.folder") + File.separator + "config" + File.separator + "registries" + File.separator
        RegistryLoader.loadBitstreamFormats(ctx, base + "bitstream-formats.xml")
        MetadataImporter.loadRegistry(base + "dublin-core-types.xml", true)
        MetadataImporter.loadRegistry(base + "sword-metadata.xml", true)
        ctx.commit
        eperson = EPerson.find(ctx, 1)
        if (eperson == null) {
          eperson = EPerson.create(ctx)
          eperson.setFirstName("first")
          eperson.setLastName("last")
          eperson.setEmail("test@email.com")
          eperson.setCanLogIn(true)
        }
        DSIndexer.cleanIndex(ctx)
        DSIndexer.createIndex(ctx)
        ctx.commit
        val indexer: IndexBrowse = new IndexBrowse(ctx)
        indexer.setRebuild(true)
        indexer.setExecute(true)
        indexer.initBrowse
      }
      ctx.restoreAuthSystemState
      if (ctx.isValid) {
        ctx.complete
      }
      ctx = null
    }
    catch {
      case ex: BrowseException => {
        log.error("Error creating the browse indexes", ex)
        fail("Error creating the browse indexes")
      }
      case ex: RegistryImportException => {
        log.error("Error loading default data", ex)
        fail("Error loading default data")
      }
      case ex: NonUniqueMetadataException => {
        log.error("Error loading default data", ex)
        fail("Error loading default data")
      }
      case ex: ParserConfigurationException => {
        log.error("Error loading default data", ex)
        fail("Error loading default data")
      }
      case ex: SAXException => {
        log.error("Error loading default data", ex)
        fail("Error loading default data")
      }
      case ex: TransformerException => {
        log.error("Error loading default data", ex)
        fail("Error loading default data")
      }
      case ex: AuthorizeException => {
        log.error("Error loading default data", ex)
        fail("Error loading default data")
      }
      case ex: SQLException => {
        log.error("Error initializing the database", ex)
        fail("Error initializing the database")
      }
      case ex: IOException => {
        log.error("Error initializing tests", ex)
        fail("Error initializing tests")
      }
    }
  }

  /**
   * This method will be run after all tests finish as per @AfterClass. It will
   * clean resources initialized by the @BeforeClass methods.
   */
  /*@AfterClass*/
  override def afterAll(): Unit = { // After All
    try {
      val origin: URL = ClassLoader.getSystemResource("dspaceFolder")
      val source: File = new File(origin.getPath)
      val dspaceTmp: File = new File(testProps.getProperty("test.folder"))
      deleteDir(source, dspaceTmp)
      val destParent: File = new File(testProps.getProperty("test.folder.assetstore"))
      deleteDir(destParent, destParent)
      testProps.clear
      testProps = null
    }
    catch {
      case ex: IOException => {
        log.error("Error cleaning the temporal files of testing", ex)
      }
    }
    super.afterAll()
  }

  /**
   * Copies one directory (And its contents) into another
   *
   * @param from Folder to copy
   * @param to Destination
   * @throws IOException There is an error while copying the content
   */
  @throws(classOf[IOException])
  protected def copyDir(from: File, to: File) {
    if (!from.isDirectory || !to.isDirectory) {
      throw new IOException("Both parameters must be directories. from is " + from.isDirectory + ", to is " + to.isDirectory)
    }
    val contents: Array[File] = from.listFiles
    for (f <- contents) {
      if (f.isFile) {
        val copy: File = new File(to.getAbsolutePath + File.separator + f.getName)
        copy.createNewFile
        copyFile(f, copy)
      }
      else if (f.isDirectory) {
        val copy: File = new File(to.getAbsolutePath + File.separator + f.getName)
        copy.mkdir
        copyDir(f, copy)
      }
    }
  }

  /**
   * Removes the copies of the origin files from the destination folder. Used
   * to remove the temporal copies of files done for testing
   *
   * @param from Folder to check
   * @param to Destination from which to remove contents
   * @throws IOException There is an error while copying the content
   */
  @throws(classOf[IOException])
  protected def deleteDir(from: File, to: File) {
    if (!from.isDirectory || !to.isDirectory) {
      throw new IOException("Both parameters must be directories. from is " + from.isDirectory + ", to is " + to.isDirectory)
    }
    val contents: Array[File] = from.listFiles
    for (f <- contents) {
      if (f.isFile) {
        val copy: File = new File(to.getAbsolutePath + File.separator + f.getName)
        if (copy.exists) {
          copy.delete
        }
      }
      else if (f.isDirectory) {
        val copy: File = new File(to.getAbsolutePath + File.separator + f.getName)
        if (copy.exists && copy.listFiles.length > 0) {
          deleteDir(f, copy)
        }
        copy.delete
      }
    }
  }

  /**
   * Copies one file into another
   *
   * @param from File to copy
   * @param to Destination of copy
   * @throws IOException There is an error while copying the content
   */
  @throws(classOf[IOException])
  protected def copyFile(from: File, to: File) {
    if (!from.isFile || !to.isFile) {
      throw new IOException("Both parameters must be files. from is " + from.isFile + ", to is " + to.isFile)
    }
    val in: FileChannel = (new FileInputStream(from)).getChannel
    val out: FileChannel = (new FileOutputStream(to)).getChannel
    in.transferTo(0, from.length, out)
    in.close
    out.close
  }

  /**
   * This method will be run before every test as per @Before. It will
   * initialize resources required for the tests.
   *
   * Other methods can be annotated with @Before here or in subclasses
   * but no execution order is guaranteed
   */
  /*@Before*/
  override def beforeEach():Unit = { //Before
    try {
      context = new Context
      context.setCurrentUser(eperson)
      context.commit
    }
    catch {
      case ex: SQLException => {
        log.error(ex.getMessage, ex)
        fail("SQL Error on AbstractUnitTest init()")
      }
    }
    super.beforeEach()
  }

  /**
   * This method will be run after every test as per @After. It will
   * clean resources initialized by the @Before methods.
   *
   * Other methods can be annotated with @After here or in subclasses
   * but no execution order is guaranteed
   */
  /*@After*/
  override def afterEach: Unit = { //After
    super.afterEach()
    if (context != null && context.isValid) {
      context.abort
    }
  }



}
