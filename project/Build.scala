import sbt._
import Keys._
/*
object HelloBuild extends Build {
  val hwsettings = Defaults.defaultSettings ++ Seq(
    organization := "syrix",
    name := "hamplate",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.9.0-1")

  import HamplateKeys._
  object HamplateKeys {
    val hpt = TaskKey[Unit]("hpt", "Compile hamplate sources.")
    val sourceDir = SettingKey[String]("source_dir", "The source directory of all *.coffee files.")
    val outputDir = SettingKey[String]("output_dir", "The output directory for Plays *.scala.html files.")
  }

  val sourceDirSetting = sourceDir := "src/main/resources"
  val outputDirSetting = outputDir := "app/views/out"

  //def srcDir = sourceDir := "."

  def hamplateCompilerTask = hpt <<=
    (sourceDir in hpt, outputDir in hpt, baseDirectory in Compile) map {
      (srcPath, outPath, base) =>
        val src = base + "/" + srcPath
        val out = base + "/" + outPath

        println("src: " + src + ",\n out: " + out + "/" + out)
        hamplate.FileManager.compile(src, out)
    }

  lazy val project = Project(
    "project",
    file("."),
    settings = hwsettings ++ Seq(sourceDirSetting, outputDirSetting, hamplateCompilerTask))
}

*/