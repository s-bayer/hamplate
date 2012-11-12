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
  /**
   * Read a resource file an split it into lines
   */
  def readLines(path: String) : Seq[String] = {
    val classLoader: ClassLoader = Thread.currentThread().getContextClassLoader();
    val url: URL = classLoader.getResource(path);
    val file: File = new File(url.toURI());
    
    val source = Source.fromFile(file)
    val lines = for (line <- source.getLines) yield line
    lines.toSeq
  }
}