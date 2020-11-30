
import Settings.{projectSettings, client => cli, server => ser, shared => sha}
import sbt.enablePlugins

lazy val root = project
  .settings(
    name := "slinky-todos",
    commands += ReleaseCmd)
  .in(file("."))
  .aggregate(client)
  .configure(
    projectSettings
  )

lazy val shared =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .settings(name := "slinky-todos-shared")
    .configure(
      projectSettings,
      sha.deps
    )

lazy val client =
  project
    .in(file("./client"))
    .settings(name := "slinky-todos-client")
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
    .settings(name := "slinky-todos-server")
    .dependsOn(shared.jvm)
    .configure(
      projectSettings,
      ser.deps,
      sha.deps
    ).enablePlugins(JavaAppPackaging)

lazy val ReleaseCmd = Command.command("release") {
  state =>
    "clean" ::
      "build" ::
      "server/compile" ::
      "server/stage" ::
      state
}


