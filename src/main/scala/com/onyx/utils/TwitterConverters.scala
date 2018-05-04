package com.onyx.utils

import com.twitter.util.{Return, Throw}
import com.twitter.{util => twitter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.language.implicitConversions
import scala.util.{Failure, Success}

object TwitterConverters {

  implicit def scalaToTwitterFuture[T](f: Future[T]): twitter.Future[T] = {
    val promise = twitter.Promise[T]()
    f onComplete {
      case Success(v) => promise.setValue(v)
      case Failure(t) => promise.setException(t)
    }
    promise
  }

  implicit def twitterToScalaFuture[T](f: twitter.Future[T]): Future[T] = {
    val promise = Promise[T]()
    f respond {
      case Return(value) => promise success value
      case Throw(exception) => promise failure exception
    }
    promise.future
  }
}
