package com.onyx.service

import com.onyx.db.LocaleRepository
import com.onyx.domain.Types.{Locale, LocaleRequest}
import com.onyx.utils.TwitterFutures
import com.twitter.util.Future
import io.scalaland.chimney.dsl._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

class LocaleServiceSpec extends WordSpec with Matchers with TwitterFutures with MockitoSugar {

  val localeRepository             = mock[LocaleRepository]
  val localeService: LocaleService = new LocaleService(localeRepository)

  "Locale service" should {
    "return current time" in {
      val localeRequest = LocaleRequest("ar", "TN")
      when(localeRepository.insert(any[Locale])).thenReturn(Future.value(Some(localeRequest.transformInto[Locale])))

      def prg = localeService.currentTime(localeRequest)

      whenReady(prg.value) { result =>
        result.isRight shouldBe true
      }
    }

    "return locale service error" in {
      val localeRequest = LocaleRequest("ar", "TN")
      when(localeRepository.insert(any[Locale])).thenReturn(Future.value(None))

      def prg = localeService.currentTime(localeRequest)

      whenReady(prg.value) { result =>
        result.isRight shouldBe false
        result.left.get shouldBe LocaleServiceError.LocaleNotFound
      }
    }
  }
}
