
import Settings.{client => cli, server => ser, shared => sha, _}

lazy val root = project
  .settings(
    name := projectName,
    commands += ReleaseCmd)
  .in(file("."))
  .aggregate(client)
  .configure(
    projectSettings
  )

lazy val shared =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .settings(name := s"$projectName-shared")
    .configure(
      projectSettings,
      sha.deps
    )

lazy val client =
  project
    .in(file("./client"))
    .settings(name := s"$projectName-client")
    .dependsOn(shared.js)
    .enablePlugins(
      ScalablyTypedConverterPlugin,
      ScalaJSBundlerPlugin,
      ScalaJSPlugin
    )
    .configure(
      projectSettings,
      sha.deps,
      cli.slinkyBasics,
      cli.webpackSettings,
      cli.antdSettings
    )

lazy val server =
  project
    .settings(name := s"$projectName-server")
    .dependsOn(shared.jvm)
    .configure(
      projectSettings,
      ser.deps,
      ser.http4s,
      ser.docker,
      sha.deps
    ).enablePlugins(JavaAppPackaging)

lazy val ReleaseCmd = Command.command("release") {
  state =>
    "client/clean" ::
      "build" ::
      "server/clean" ::
      "server/compile" ::
      "server/stage" ::
      state
}


