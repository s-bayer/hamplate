package hamplate

import scala.io.Source
import java.io.InputStream
import java.io.BufferedReader
import java.io.StringWriter
import java.io.Writer
import java.io.Reader
import java.io.InputStreamReader
import java.io.File
import java.net.URL

object FileReader {
  def test(path: String) = {
    val classLoader: ClassLoader = Thread.currentThread().getContextClassLoader();
    val url: URL = classLoader.getResource(path);
    val file: File = new File(url.toURI());
    for (file <- file.listFiles) { println(file) }
  }

  /**
   * Read a resource file an split it into lines
   */
  def readLines(path: String) {
    /*val inputStream:InputStream = getClass().getResourceAsStream(path)
    
    val file:String = convertStreamToString(inputStream)
    
    for (line <- file.split("\n"))
      println(line)*/
    /*val source = Source.fromFile(path)
    for (line <- source.getLines)
      println(line)*/
    val classLoader: ClassLoader = Thread.currentThread().getContextClassLoader();
    val url: URL = classLoader.getResource(path);
    val file: File = new File(url.toURI());
    
    val source = Source.fromFile(file)
    for (line <- source.getLines)
      println(line)
  }

  // TODO evil java code
  private def convertStreamToString(is: InputStream): String =
    if (is != null) {
      val writer: Writer = new StringWriter()

      var buffer: Array[Char] = new Array[Char](1024)
      try {
        val reader: Reader = new BufferedReader(
          new InputStreamReader(is, "UTF-8"))
        var n: Int = reader.read(buffer)
        while (n != -1) {
          writer.write(buffer, 0, n)
          n = reader.read(buffer)
        }
      } finally {
        is.close()
      }
      writer.toString()
    } else {
      println("could not read file: " + is)
      ""
    }
}