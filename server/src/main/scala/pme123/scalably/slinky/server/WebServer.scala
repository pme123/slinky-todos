package pme123.scalably.slinky.server

import java.net.{InetSocketAddress, URLConnection}
import java.nio.ByteBuffer

import boopickle.Default._
import boopickle.UnpickleImpl
import pme123.scalably.slinky.shared.Api
import uzhttp.Request.Method
import uzhttp.server.Server
import uzhttp.{HTTPError, Request, Response}
import zio._

import scala.concurrent.ExecutionContext.global

object WebServer extends App {

  override def run(args: List[String]): URIO[ZEnv, Nothing] =
    Server
      .builder(new InetSocketAddress("localhost", 8881))
      .handleSome {
        case req if req.method == Method.OPTIONS =>
          // autowire sends that
          ZIO.succeed(
            Response
              .const(Array.empty, headers = List("Allow" -> "OPTIONS, POST"))
          )
        case req if req.uri.getPath.startsWith("/api") =>
          autowireApi(req)

        case req if req.uri.getPath.length <= 1 =>
          Response
            .fromResource(
              s"index.html",
              req,
              contentType = "text/html"
            )
            .refineHTTP(req)
        case req =>
          Response
            .fromResource(
              req.uri.getPath.drop(1),
              req,
              contentType = req.uri.getPath match {
                case p if p.endsWith(".svg") =>
                  "image/svg+xml"
                case _ =>
                  URLConnection.guessContentTypeFromName(req.uri.getPath)
              }
            )
            .refineHTTP(req)
      }
      .serve
      .useForever
      .orDie

  private def autowireApi(request: Request) = {
    (for {
      path <- UIO(request.uri.getPath.split("/").filter(_.nonEmpty).tail)
      _ <- console.putStrLn(s"Request path: ${path.toSeq}")
      array <-
        if (request.body.nonEmpty)
          request.body.get.runCollect.map(c => Some(c.toArray))
        else
          ZIO.none
      futResult <- ZIO(inputToOutput(path, array))
      result <- ZIO.fromFuture(implicit global => futResult)
    } yield Response.const(
      result.array()))
      .flatMapError {
        case ex: Throwable =>
          console.putStrLnErr(ex.getStackTrace.mkString("\n")) *>
            UIO(HTTPError.InternalServerError(ex.getMessage))
        case other =>
          console.putStrLnErr(s"Error: $other") *>
            UIO(HTTPError.InternalServerError(s"Exception: $other"))
      }

  }

  val apiService = new ApiService()
  implicit val ec: scala.concurrent.ExecutionContext = global

  private def inputToOutput(path: Seq[String], body: Option[Array[Byte]]) = {
    // call Autowire route
    val args = body
      .map(array =>
        // Unpickle was not correct in Intellij > UnpickleImpl
        UnpickleImpl[Map[String, ByteBuffer]].fromBytes(ByteBuffer.wrap(array))
      )
      .getOrElse(Map.empty)
    ApiRouter
      .route[Api](apiService)(
        autowire.Core.Request(path, args)
      )
      .map(buffer => {
        val data = Array.ofDim[Byte](buffer.remaining())
        buffer.get(data)
      })
  }
}

