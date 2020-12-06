import com.typesafe.sbt.packager.Keys.dockerExposedPorts
import com.typesafe.sbt.packager.docker.DockerPlugin
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import org.scalablytyped.converter.plugin.ScalablyTypedPluginBase.autoImport._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object Settings {

  lazy val projectSettings: Project => Project =
    _.settings(
      version := "0.2.0",
      scalaVersion := "2.13.2"
    )

  object shared {
    lazy val deps: Project => Project =
      _.settings(
        libraryDependencies ++= Seq(
          "com.lihaoyi" %%% "autowire" % "0.3.2",
          "io.suzaku" %%% "boopickle" % "1.3.2",
          "com.lihaoyi" %%% "upickle" % "1.2.2"
        )
      ) //.withoutSuffixFor(JVMPlatform)
  }

  object server {
    lazy val deps: Project => Project =
      _.settings(
        Compile / unmanagedResourceDirectories += baseDirectory.value / "../client/target/build"
      )

    val http4sVersion = "0.21.12"
    lazy val http4s: Project => Project =
      _.settings(
        libraryDependencies ++= Seq(
          "org.http4s" %% "http4s-dsl" % http4sVersion,
          "org.http4s" %% "http4s-blaze-server" % http4sVersion
        )
      )

    lazy val docker: Project => Project =
      _.settings(
        dockerExposedPorts ++= Seq(8883)
      ).enablePlugins(DockerPlugin)
  }

  object client {

    lazy val slinkyBasics: Project => Project =
      _.settings(
        scalacOptions += "-Ymacro-annotations",
        requireJsDomEnv in Test := true,
        addCommandAlias("dev", ";set javaOptions  += \"-DIsLocal=true\";fastOptJS::startWebpackDevServer;~fastOptJS"),
        addCommandAlias("build", "fullOptJS::webpack"),
        libraryDependencies ++= Seq(
          "me.shadaj" %%% "slinky-web" % "0.6.6",
          "me.shadaj" %%% "slinky-hot" % "0.6.6"
        ),
        libraryDependencies ++= Seq(
          "org.scalatest" %%% "scalatest" % "3.1.1" % Test
        ),
        Compile / npmDependencies ++= Seq(
          "react" -> "16.13.1",
          "react-dom" -> "16.13.1",
          "react-proxy" -> "1.1.8"
        ),
        Compile / npmDevDependencies ++= Seq(
          "file-loader" -> "6.0.0",
          "style-loader" -> "1.2.1",
          "css-loader" -> "3.5.3",
          "html-webpack-plugin" -> "4.3.0",
          "copy-webpack-plugin" -> "5.1.1",
          "webpack-merge" -> "4.2.2"
        )
      )


    lazy val antdSettings: Project => Project =
      _.settings(
        stFlavour := Flavour.Slinky,
        useYarn := true,
        stIgnore := List("react-proxy"),
        Compile / npmDependencies ++= Seq(
          "antd" -> "4.5.1",
          "@types/react" -> "16.9.42",
          "@types/react-dom" -> "16.9.8"
        )
      )

    lazy val webpackSettings: Project => Project =
      _.settings(
        webpackDevServerPort := 8024,
        version in webpack := "4.43.0",
        version in startWebpackDevServer := "3.11.0",
        webpackResources := baseDirectory.value / "webpack" * "*",
        webpackConfigFile in fastOptJS := Some(
          baseDirectory.value / "webpack" / "webpack-fastopt.config.js"
        ),
        webpackConfigFile in fullOptJS := Some(
          baseDirectory.value / "webpack" / "webpack-opt.config.js"
        ),
        webpackConfigFile in Test := Some(
          baseDirectory.value / "webpack" / "webpack-core.config.js"
        ),
        webpackDevServerExtraArgs in fastOptJS := Seq("--inline", "--hot", "--disableHostCheck"),
        webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly()
      )

  }

}
