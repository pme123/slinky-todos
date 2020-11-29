package pme123.scalably.slinky.server

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import boopickle.Default._
import pme123.scalably.slinky.shared.Api
import uzhttp.Request.Method
import uzhttp.server.Server
import uzhttp.websocket.Frame
import uzhttp.{HTTPError, RefineOps, Request, Response}
import zio._
import zio.stream.ZTransducer

import scala.concurrent.ExecutionContext.global

object WebServer extends App {

  override def run(args: List[String]) =
    Server
      .builder(new InetSocketAddress("localhost", 8888))
      .handleSome {
        case req if req.method == Method.OPTIONS =>
          // autowire sends that
          ZIO.succeed(
            Response
              .const(Array.empty, headers = List("Allow" -> "OPTIONS, POST"))
          )
        case req if req.uri.getPath.startsWith("/api") =>
          autowireApi(req)
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
          ZIO(None)
      futResult <- ZIO(inputToOutput(path, array))
      result <- ZIO.fromFuture(global => futResult)
    } yield Response.const(
      result.array(),
      headers = (List("Access-Control-Allow-Origin" -> "*"))
    ))
      .mapError {
        case ex: Throwable =>
          ex.printStackTrace()
          HTTPError.InternalServerError(ex.getMessage)
        case other =>
          println(s"Error: $other")
          HTTPError.InternalServerError(s"Exception: $other")
      }

  }
  val apiService = new ApiService()
  implicit val ec: scala.concurrent.ExecutionContext = global

  private def inputToOutput(path: Seq[String], body: Option[Array[Byte]]) = {
    // call Autowire route
    val args = body
      .map(array =>
        Unpickle[Map[String, ByteBuffer]].fromBytes(ByteBuffer.wrap(array))
      )
      .getOrElse(Map.empty)
    Router
      .route[Api](apiService)(
        autowire.Core.Request(path, args)
      )
      .map(buffer => {
        val data = Array.ofDim[Byte](buffer.remaining())
        buffer.get(data)
      })
  }
}

object PeopleService {}

import boopickle.Default._

// server side
object Router extends autowire.Server[ByteBuffer, Pickler, Pickler] {
  override def read[R: Pickler](p: ByteBuffer) = Unpickle[R].fromBytes(p)
  override def write[R: Pickler](r: R) = Pickle.intoBytes(r)
}
