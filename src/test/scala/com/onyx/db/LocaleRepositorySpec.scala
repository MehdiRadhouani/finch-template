package com.onyx.db

import com.github.simplyscala.MongoEmbedDatabase
import com.onyx.domain.Types.Locale
import com.onyx.utils.TwitterFutures
import com.twitter.util.Future
import org.scalatest._
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global

class LocaleRepositorySpec extends WordSpec with MongoEmbedDatabase with TwitterFutures with Matchers {

  implicit val asyncConfig = PatienceConfig(timeout = scaled(Span(2, Seconds)))

  "Locale repository" should {
    "perform CRUD operations" in {
      withEmbedMongoFixture() { mongodProps =>
        val localeRepository = new LocaleRepository
        val localeTN = Locale(language = "ar", country = "TN")
        val localeNL = Locale(language = "nl", country = "NL")
        val localeFR = Locale(language = "fr", country = "FR")

        def prg1: Future[List[Locale]] = for {
          _ <- localeRepository.insert(localeTN)
          _ <- localeRepository.insert(localeNL)
          _ <- localeRepository.insert(localeFR)
          result <- localeRepository.findAll
        } yield result


        whenReady(prg1) { result =>
          result.size shouldBe 3
        }

        val localeBE = Locale(language = "fr", country = "BE")

        def prg2: Future[Option[Locale]] = localeRepository.insert(localeBE).flatMap { v =>
          localeRepository.update(localeBE._id, localeBE.copy(country = "NL"))
        }

        whenReady(prg2) { result =>
          result.get shouldBe localeBE.copy(country = "NL")
        }

        val localeUS = Locale(language = "us", country = "US")

        def prg3: Future[Boolean] = for {
          _ <- localeRepository.insert(localeUS)
          result <- localeRepository.delete(localeUS._id)
        } yield result

        whenReady(prg3) { result =>
          result shouldBe true
        }
      }
    }
  }

}