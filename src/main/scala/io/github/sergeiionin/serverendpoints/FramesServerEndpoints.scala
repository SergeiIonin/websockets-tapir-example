package io.github.sergeiionin.serverendpoints

import cats.effect.IO
import sttp.capabilities.WebSockets
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.server.ServerEndpoint
import io.github.sergeiionin.endpoints.WsEndpoints

class FramesServerEndpoints(stream: fs2.Stream[IO, String]) extends WsEndpoints:
  val framesServerEndpoints: List[ServerEndpoint[Fs2Streams[IO] & WebSockets, IO]] = List(framesServerEndpoint())

  def framesServerEndpoint() = 
    framesEndpoint
    .serverLogic[IO](_ =>
      IO.pure(
        Right((_: fs2.Stream[IO, String]) =>
          stream
        )
      )
    )