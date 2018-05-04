package com.onyx.db

import com.onyx.domain.Types.Locale
import com.onyx.utils.TwitterConverters._
import com.twitter.util.Future
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{ BSONDocument, BSONObjectID }

import scala.concurrent.ExecutionContext

class LocaleRepository(implicit executionContext: ExecutionContext) {

  val localeCollection: BSONCollection = MongoDBConnection.database.collection("locale")

  def findAll: Future[List[Locale]] = localeCollection.find(BSONDocument()).cursor[Locale]().collect[List]()

  def findById(id: BSONObjectID): Future[Option[Locale]] = localeCollection.find(BSONDocument("_id" -> id)).one[Locale]

  def insert(locale: Locale): Future[Option[Locale]] =
    for {
      _        <- localeCollection.insert(locale)
      toReturn <- findById(locale._id)
    } yield toReturn

  def update(key: BSONObjectID, locale: Locale): Future[Option[Locale]] =
    for {
      _        <- localeCollection.update(BSONDocument("_id" -> key), locale)
      toReturn <- findById(key)
    } yield toReturn

  def delete(key: BSONObjectID): Future[Boolean] =
    localeCollection.remove(BSONDocument("_id" -> key)).map(_.ok)
}
