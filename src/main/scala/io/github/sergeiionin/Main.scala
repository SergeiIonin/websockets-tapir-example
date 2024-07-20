package io.github.sergeiionin

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.comcast.ip4s.{Port, host, port}
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.server.Server
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.*
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.capabilities.WebSockets
import sttp.tapir.server.ServerEndpoint
import io.github.sergeiionin.serverendpoints.FramesServerEndpoints

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.*

/**
 * Open jokes.html in a browser to see the messages flooding the screen
 */

object Main extends IOApp:
  private val host       = host"localhost"
  private val port       = port"8080"
  def serverRoutes(
                    serverEndpoints: List[ServerEndpoint[Fs2Streams[IO] & WebSockets, IO]],
                    wsb2:            WebSocketBuilder2[IO],
                  ): HttpRoutes[IO] =
    Http4sServerInterpreter[IO]()
      .toWebSocketRoutes(serverEndpoints)(wsb2)

  def buildServer(serverEndpoints: List[ServerEndpoint[Fs2Streams[IO] & WebSockets, IO]]): Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(host)
      .withPort(port)
      .withHttpWebSocketApp(wsb2 => serverRoutes(serverEndpoints, wsb2).orNotFound)
      .build

  val jokesService: JokesService = JokesService()
  
  override def run(args: List[String]): IO[ExitCode] =
    val stream = fs2.Stream.fixedDelay[IO](3.second).map(_ => s"new joke: ${jokesService.getJoke}")
    val serverEndpoints = FramesServerEndpoints(stream)
    val server = buildServer(serverEndpoints.framesServerEndpoints)
    server.useForever.void
      .as(ExitCode.Success)

