package com.onyx.service

import java.util.Calendar

import cats.data.EitherT
import com.onyx._
import com.onyx.db.LocaleRepository
import com.onyx.domain.Types.{ Locale, LocaleRequest, Time }
import com.onyx.service.LocaleServiceError.LocaleNotFound
import com.twitter.util.Future
import io.scalaland.chimney.dsl._

class LocaleService(localeRepository: LocaleRepository) {

  def currentTime(localeRequest: LocaleRequest): EitherT[Future, LocaleServiceError, Time] = {
    val locale = localeRequest.transformInto[Locale]
    for {
      l <- EitherT.fromOptionF(localeRepository.insert(locale), LocaleNotFound.asInstanceOf[LocaleServiceError])
    } yield {
      val locale = new java.util.Locale(l.language, l.country)
      Time(l, Calendar.getInstance(locale).getTime.toString)
    }
  }
}

sealed trait LocaleServiceError

object LocaleServiceError {
  final case object LocaleNotFound extends LocaleServiceError
}
