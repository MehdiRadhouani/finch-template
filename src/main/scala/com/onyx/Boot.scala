package com.onyx

import com.onyx.db.LocaleRepository
import com.onyx.http.Endpoints
import com.onyx.service.LocaleService
import com.twitter.finagle.Http
import com.twitter.util.Await
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext.Implicits.global

object Boot extends App {

  // $COVERAGE-OFF$
  private val config = ConfigFactory.load()

  val localeRepository             = new LocaleRepository
  val localeService: LocaleService = new LocaleService(localeRepository)

  Await.ready(Http.server.serve(":8081", Endpoints(localeService).service))
  // $COVERAGE-ON$
}
