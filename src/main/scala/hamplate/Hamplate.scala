package hamplate

import sbt._
import Keys._

object Hamplate extends Plugin {
  import HamplateKeys._
  object HamplateKeys {
    val hpt = TaskKey[Unit]("hpt", "Compile hamplate sources.")
    val sourceDir = SettingKey[String]("source_dir", "The source directory of all *.coffee files.")
    val outputDir = SettingKey[String]("output_dir", "The output directory for Plays *.scala.html files.")
  }

  //def srcDir = sourceDir := "."

  def hamplateCompilerTask = hpt <<=
    (sourceDir in hpt, outputDir in hpt, baseDirectory in Compile) map {
      (srcPath, outPath, base) =>
        val src = base + "/" + srcPath
        val out = base + "/" + outPath

        println("src: " + src + ",\n out: " + out + "/" + out)
        hamplate.FileManager.compile(src, out)
    }

  val hamplateSettings = Seq(
    sourceDir := "src/main/resources",
    outputDir := "app/views/out")
}