package hamplate

import java.io.File
import scala.io.Source
import java.io.PrintWriter

object FileManager {
  private val extension = ".hpt"

  private def printToFile(f: File)(op: PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }

  private def save(f: File, content: String) {
    printToFile(f)(p =>
      p.print(content))
  }

  private def compile(file: File, targetDir: String) {
    print("* " + file.getName() + ".")

    // split into lines
    val source = Source.fromFile(file)
    val lines = for (line <- source.getLines) yield line
    val split = lines.toSeq
    print(".")

    // tokenize
    val tokens = Tokenizer.tokenize(split)
    print(".")

    // parse
    val output = ASTBuilder.build(tokens).toHtml
    print(".")

    // write to file

    println(".")
    save(new File(targetDir + "/" + file.getName().dropRight(extension.size) + ".scala.html"), output)
  }

  def compile(sourceDir: String, targetDir: String) {
    try {
      val files = new java.io.File(sourceDir).listFiles.filter(_.getName.endsWith(extension))
      println("Compiling " + files.size + " files in folder " + sourceDir)
      files.map { compile(_, targetDir) }
    } catch {
      case e: NullPointerException => throw new Error("source folder ( " + sourceDir + " ) doesn't exist.");
    }
  }
}