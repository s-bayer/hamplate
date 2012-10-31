package hamplate

object FileManager {
  def compile(sourceDir: String, targetDir: String) {
    new java.io.File(sourceDir).listFiles.filter(_.getName.endsWith("")).map { println(_) }
  }
}