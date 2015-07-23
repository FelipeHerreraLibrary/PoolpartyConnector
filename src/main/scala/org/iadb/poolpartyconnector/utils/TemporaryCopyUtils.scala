package org.iadb.poolpartyconnector.utils

import java.io.{File, InputStream}
import java.nio.file.{Files, StandardCopyOption}

/**
 * Created by Daniel Maatari Okouya on 6/6/15.
 */
object TemporaryCopyUtils {


  /**
   * Copy the content of an inputstream into a newly created temporary file and returns it
   *
   * @param stream A stream taken out of a file.
   * @return
   */
  def getemporaryCopy(stream: InputStream) = {

    val temporaryCopy = File.createTempFile("toClassify", null)

    println("\n\nThe size of the temp file before:" + temporaryCopy.getFreeSpace + "\n\n")

    println("Number of Bytes written" + Files.copy(stream, temporaryCopy.toPath, StandardCopyOption.REPLACE_EXISTING))

    println("\n\nThe size of the temp file after:" + temporaryCopy.getFreeSpace + "\n\n")
    temporaryCopy

  }


  /**
   * Delete a temporary file
   *
   * Return true is the operation is a sucess and false otherwise
   *
   */
  def deleteTemporaryCopy(tempFile: File) : Boolean = {

    Files.delete(tempFile.toPath)

    true

  }

}
