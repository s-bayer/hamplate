import sbt._
import Keys._

object Hamplate extends Plugin {
  import HamplateKeys._
  object HamplateKeys {
    //val hpt = TaskKey[Unit]("hpt", "Compile hamplate sources.")
    val sourceDir = SettingKey[String]("source_dir", "The source directory of all *.coffee files.")
    val outputDir = SettingKey[String]("output_dir", "The output directory for Plays *.scala.html files.")
  }

  override lazy val settings = Seq(commands ++= Seq( /*myCommand, */ hpt))

  /*lazy val myCommand =
    Command.command("hello") { (state: State) =>
      println("Hi!")
      state
    }*/

  // A simple, multiple-argument command that prints "Hi" followed by the arguments.
  //   Again, it leaves the current state unchanged.
  lazy val hpt = Command.args("hpt", "<src=<dirname> >, <out=<dirname> >") { (state, args) =>
    var src = "app/views"
    var out = "app/views"
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
    sourceDir := "src/main/resources",
    outputDir := "app/views/out")
}