package com.onyx.domain

import java.util.{ Calendar, Date }

import reactivemongo.bson.BSONObjectID

object Types {

  case class LocaleRequest(language: String, country: String)
  case class Locale(_id: BSONObjectID = BSONObjectID.generate(),
                    language: String,
                    country: String,
                    createdDate: Date = Calendar.getInstance().getTime)
  case class Time(locale: Locale, time: String)

}
