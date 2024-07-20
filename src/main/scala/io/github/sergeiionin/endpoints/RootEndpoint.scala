package io.github.sergeiionin.endpoints

import sttp.tapir.*

trait RootEndpoint:
  protected val rootEndpoint = endpoint.in("ws")
