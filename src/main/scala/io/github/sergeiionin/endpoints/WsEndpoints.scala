package io.github.sergeiionin.endpoints

import cats.effect.IO
import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.capabilities.fs2.Fs2Streams

trait WsEndpoints extends RootEndpoint:
  protected def wsEndpoints: List[AnyEndpoint] = List(framesEndpoint)

  protected val framesEndpoint = 
    rootEndpoint
    .in("frames")
    .out(webSocketBody[String, CodecFormat.Json, String, CodecFormat.Json](Fs2Streams[IO]))
