package pme123.scalably.slinky.services

import java.nio.ByteBuffer

import boopickle.Default._
import boopickle.UnpickleImpl
import org.scalajs.dom.ext.Ajax

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}

object AjaxClient extends autowire.Client[ByteBuffer, Pickler, Pickler] {
  implicit val ec: ExecutionContext = ExecutionContext.global

  override def doCall(req: Request): Future[ByteBuffer] = {
    println(s"DATA: ${req.args}")
    Ajax.post(
      url = "http://localhost:8888/api/" + req.path.mkString("/"),
      data = Pickle.intoBytes(req.args),
      responseType = "arraybuffer",
      headers = Map("Content-Type" -> "application/octet-stream")
    ).map(r => TypedArrayBuffer.wrap(r.response.asInstanceOf[ArrayBuffer]))
  }

  override def read[Result: Pickler](p: ByteBuffer): Result = UnpickleImpl[Result].fromBytes(p)

  override def write[Result: Pickler](r: Result): ByteBuffer = Pickle.intoBytes(r)
}
