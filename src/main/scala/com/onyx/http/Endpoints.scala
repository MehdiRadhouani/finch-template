package com.onyx.http

import com.onyx.domain.Types.{ LocaleRequest, Time }
import com.onyx.service.LocaleService
import com.twitter.finagle.Service
import com.twitter.finagle.http.{ Request, Response }
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.syntax.post
import io.finch.{ Endpoint, Ok, jsonBody, _ }
import com.onyx._

object Endpoints {
  def apply(localeService: LocaleService) = new Endpoints(localeService)
}

class Endpoints(localeService: LocaleService) {

  val time: Endpoint[Time] =
    post("time" :: jsonBody[LocaleRequest]) { localeRequest: LocaleRequest =>
      localeService.currentTime(localeRequest).fold(error => BadRequest(new Exception(error.toString)), Ok)
    }

  val service: Service[Request, Response] = time.toService
}
