package example

import zio._
import zio.console._
import zio.interop.catz._
import zio.interop.catz.implicits._

import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

object Example extends App {

  private val dsl = Http4sDsl[Task]
  import dsl._

  private val helloWorldService = HttpRoutes
    .of[Task] {
      case request @ GET -> Root =>
        StaticFile
          .fromResource("/home.html", Some(request))
          .getOrElseF(InternalServerError())

      case request @ GET -> Root / "js" / jsFile
          if jsFile.endsWith(".js") || jsFile.endsWith(".map") =>
        StaticFile
          .fromResource(s"/$jsFile", Some(request))
          .getOrElseF(InternalServerError())
    }
    .orNotFound

  def run(args: List[String]): zio.URIO[zio.ZEnv, ExitCode] =
    ZIO
      .runtime[ZEnv]
      .flatMap { implicit runtime =>
        BlazeServerBuilder[Task]
          .withExecutionContext(runtime.platform.executor.asEC)
          .withHttpApp(helloWorldService)
          .bindHttp(8080, "localhost")
          .resource
          .toManagedZIO
          .useForever
          .exitCode
      }


}