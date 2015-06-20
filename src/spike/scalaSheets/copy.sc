import java.io.File
import java.nio.file.Files

import org.iadb.poolpartyconnector.contentclassification.PoolPartyClassificationService

val service = new PoolPartyClassificationService("PoolPartyService")


val in = Files.newInputStream(
  new File("/Users/maatary/IdeaProjects/PoolpartyConnector/src/main/resources/Health-Benefit-Plans.pdf").toPath)


service.recommendMetadata(in)