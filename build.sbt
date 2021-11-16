
lazy val client =
  project
    .in(file("client"))
    .settings(name := "client")
    .enablePlugins(ScalaJSPlugin)
    .settings(
      libraryDependencies ++= Seq("org.scala-js" %%% "scalajs-dom" % "2.0.0")
    )
    .settings(scalaJSUseMainModuleInitializer := true)
    .settings(
      scalacOptions --= Seq(
        "-Wdead-code",
        "-Wunused:params",
        "-Wunused:explicits"
      )
    )

lazy val webserver =
  project
    .in(file("webserver"))
    .settings(
      name := "testing-scalajs"
    )
    .settings(
      libraryDependencies ++= Seq(
        "dev.zio" %% "zio" % "1.0.12",
        "dev.zio" %% "zio-interop-cats" % "3.1.1.0",
        "org.http4s" %% "http4s-blaze-server" % "0.23.6",
        "org.http4s" %% "http4s-dsl" % "0.23.6"
      )
    )
    .settings(
      Compile / compile := ((Compile / compile) dependsOn ((client / Compile) / fastOptJS)).value,
      Compile / resourceGenerators += Def.task {
        val files =
          (((client / Compile) / crossTarget).value ** ("*.js" || "*.map")).get
        println(s"Copying js resources (${files.map(_.getName)}) ...")
        files
      }
    )
