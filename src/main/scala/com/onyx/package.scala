package com

import java.util.Date

import cats.syntax.either._
import cats.{ Monad, MonadError, StackSafeMonad }
import com.onyx.domain.Types.Locale
import com.onyx.service.LocaleServiceError
import com.twitter.util.Future
import io.circe.{ Decoder, Encoder, Json }
import reactivemongo.bson.{ BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros }

package object onyx {

  //cats implicits
  implicit def catsStdInstancesForFuture: MonadError[Future, Throwable] with Monad[Future] =
    new MonadError[Future, Throwable] with Monad[Future] with StackSafeMonad[Future] {
      override def raiseError[A](e: Throwable): Future[A] = Future.exception(e)

      override def handleErrorWith[A](fa: Future[A])(f: Throwable => Future[A]): Future[A] = fa.rescue {
        case t => f(t)
      }

      override def pure[A](x: A): Future[A] = Future.value(x)

      override def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B] = fa.flatMap(f)
    }

  // Circe implicits
  implicit val encodeBSONObjectID: Encoder[BSONObjectID] = Encoder.encodeString.contramap[BSONObjectID](_.stringify)

  val formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

  implicit val encodeDate: Encoder[Date] = Encoder.encodeString.contramap[Date](formatter.format)

  implicit val encodeException: Encoder[Exception] = Encoder.instance { e =>
    Json.obj(
      "message" -> Json.fromString(e.getMessage)
    )
  }

  // ReactiveMongo implicits
  implicit def localeWriter: BSONDocumentWriter[Locale] = Macros.writer[Locale]
  implicit def localeReader: BSONDocumentReader[Locale] = Macros.reader[Locale]
}
