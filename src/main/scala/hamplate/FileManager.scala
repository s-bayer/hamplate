package hamplate

import java.io.File
import scala.io.Source
import java.io.PrintWriter

object StatusUpdater {
  private val InitStatus = "Finished"
  private val Init = "* [" + InitStatus + "] "
  def update(status: String) {
    if (status.size > InitStatus.size)
      throw new Error("Could not update the status since '" + status + "' is too long")
    else
      print("\r* [" + status.padTo(InitStatus.size, " ").mkString + "] ")
  }

  def updateUnsafe(status: String) {
    print("\r* [" + (status.padTo(InitStatus.size, " ").mkString) + "] ")
  }
}

object FileManager {
  import Printer._
  import StatusUpdater._
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
    println()
    update(".")
    print(file.getName())

    // split into lines
    val source = Source.fromFile(file)
    val lines = for (line <- source.getLines) yield line
    val split = lines.toSeq
    update("..")

    // tokenize
    val tokens = Tokenizer.tokenize(split)
    update("...")

    // parse
    val (ast, errors) = ASTBuilder.build(tokens)
    val output = ast.toHtml
    update("....")

    // write to file

    save(new File(targetDir + "/" + file.getName().dropRight(extension.size) + ".scala.html"), output)
    if (errors.isEmpty) {
      updateUnsafe(cSuccess("Finished"))
      print("\n")
    } else {
      updateUnsafe(cError("Error   "))
      print("\n" + errors.mkString("\n"))
    }
  }

  def compileFiles(src: File, targetDir: String) {
    try {
      if (src.isDirectory()) {
        val dir = src.listFiles
        val files = dir.filter(_.getName.endsWith(extension))

        println("Compiling " + files.size + " files in folder " + src.getName())
        // create output dir
        val outDir = new File(targetDir)
        outDir.mkdir()

        // actually compile files
        files.map { compile(_, targetDir) }

        for (f <- dir) {
          if (f.isDirectory) compileFiles(f, targetDir + "/" + f.getName())
        }
      } else
        throw new Error("source folder ( " + src.getName() + " ) is not a directory.")
    } catch {
      case e: NullPointerException => throw new Error("source folder ( " + src.getName() + " ) doesn't exist.")
    }
  }

  def compile(sourceDir: String, targetDir: String) {
    println("begin compiling files in '" + sourceDir + "' to '" + targetDir + "'")
    try {
      compileFiles(new File(sourceDir), targetDir)
    } catch {
      case e: NullPointerException => throw new Error("source folder ( " + sourceDir + " ) doesn't exist.")
    }
    println("compilation finished\n")
  }
}