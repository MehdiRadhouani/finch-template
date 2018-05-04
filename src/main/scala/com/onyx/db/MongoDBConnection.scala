package com.onyx.db

import com.typesafe.config.ConfigFactory
import reactivemongo.api.{ DefaultDB, MongoConnection, MongoDriver }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

object MongoDBConnection {
  private val config = ConfigFactory.load()
  val driver         = new MongoDriver

  private val uri: MongoConnection.ParsedURI =
    MongoConnection.parseURI(config getString "mongodb.uri").toOption.getOrElse(sys.error("Failed to parse uri"))
  private val connection: MongoConnection = driver.connection(uri)
  private val databaseFuture: Future[DefaultDB] =
    connection.database(uri.db.getOrElse(sys.error("Database name not found in uri")))
  val database: DefaultDB = Await.result(databaseFuture, 10.seconds)
}
