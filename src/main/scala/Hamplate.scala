import sbt._
import Keys._

object Hamplate extends Plugin {
  import HamplateKeys._
  object HamplateKeys {
    //val hpt = TaskKey[Unit]("hpt", "Compile hamplate sources.")
    val sourceDir = SettingKey[String]("source_dir", "The source directory of all *.coffee files.")
    val outputDir = SettingKey[String]("output_dir", "The output directory for Plays *.scala.html files.")
  }

  override lazy val settings = Seq(commands ++= Seq(hpt))

  // Compile all files in the source dir to the output dir
  lazy val hpt = Command.args("hpt", "<src=<dirname> >, <out=<dirname> >") { (state, args) =>
    val extracted = Project.extract(state)
    import extracted._
    var src = get(sourceDir)
    var out = get(outputDir)
    for (a <- args) {
      a.split("=") match {
        case Array("src", dir) =>
          println("setting src to: " + dir)
          src = dir
        case Array("out", dir) =>
          println("setting out to: " + dir)
          out = dir
        case _ => println("invalid argument")
      }
    }

    hamplate.FileManager.compile(src, out)
    state
  }

  //def srcDir = sourceDir := "."

  /*def hamplateCompilerTask = hpt <<=
    (sourceDir in hpt, outputDir in hpt, baseDirectory in Compile) map {
      (srcPath, outPath, base) =>
        val src = base + "/" + srcPath
        val out = base + "/" + outPath

        println("src: " + src + ",\n out: " + out + "/" + out)
        hamplate.FileManager.compile(src, out)
    }
*/
  val hamplateSettings = Seq(
    sourceDir := "app/views",
    outputDir := "app/views")
}