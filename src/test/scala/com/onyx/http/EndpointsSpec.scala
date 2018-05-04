package com.onyx.http

import cats.data.EitherT
import cats.implicits._
import com.onyx.domain.Types.{Locale, LocaleRequest, Time}
import com.onyx.service.LocaleServiceError.LocaleNotFound
import com.onyx.service.{LocaleService, LocaleServiceError}
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, RequestBuilder, Response, Status}
import com.twitter.io.Buf
import com.twitter.util.Future
import io.circe.generic.auto._
import io.circe.syntax._
import io.finch.test.ServiceSuite
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, fixture}

class EndpointsSpec extends fixture.WordSpec with ServiceSuite with Matchers with MockitoSugar {

  val localService = mock[LocaleService]

  "Endpoints" should {
    "return a valid time" in { f =>
      val localeRequest = LocaleRequest("nl", "NL")
      val time = Time(Locale(language = "nl", country = "NL"), "some Time")
      when(localService.currentTime(localeRequest)).thenReturn(EitherT(Future.value(time.asRight[LocaleServiceError])))
      val request: Request = RequestBuilder()
        .url("http://localhost:8080/time").buildPost(
        Buf.Utf8(localeRequest.asJson.noSpaces)
      )

      val result: Response = f(request)

      result.status shouldBe Status.Ok
    }

    "return BadRequest when locale not found" in { f =>
      val localeRequest = LocaleRequest("nl", "NL")
      when(localService.currentTime(localeRequest)).thenReturn(EitherT(Future.value(LocaleNotFound.asInstanceOf[LocaleServiceError].asLeft[Time])))
      val request: Request = RequestBuilder()
        .url("http://localhost:8080/time").buildPost(
        Buf.Utf8(localeRequest.asJson.noSpaces)
      )

      val result: Response = f(request)

      result.status shouldBe Status.BadRequest
      result.contentString shouldBe """{"message":"LocaleNotFound"}"""
    }
  }

  override def createService(): Service[Request, Response] = Endpoints(localService).service
}
