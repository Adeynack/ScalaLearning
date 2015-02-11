package misc

import java.util.concurrent.TimeUnit._

import org.specs2.mutable.Specification

import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Try}

class FutureAndTrySpec extends Specification {
  sequential

  "A Future depending on a Try" should {
    sequential

    "success if the Try succeeds" in {

      lazy val dependency: Try[Int] = Try {
        84
      }

      def service(): Future[Int] = Future.fromTry {
        dependency.map { d =>
          d / 2
        }
      }

      val resultFuture: Future[Int] = service()
      val resultValue = Await.result(resultFuture, Duration(1, MINUTES))

      resultValue must be equalTo 42
    }

    "fail if the service (Future) fails" in {

      lazy val dependency: Try[Int] = Try {
        84
      }

      def service(): Future[Int] = Future.fromTry {
        dependency.map { d =>
          d / 0 // throws an ArithmeticException
        }
      }

      val resultFuture: Future[Int] = service()
      Await.ready(resultFuture, Duration(1, MINUTES))

      def getValue = Await.result(resultFuture, Duration(1, MINUTES))

      getValue must throwA[ArithmeticException]
    }

    "fail if the dependency (Try) fails" in {

      lazy val dependency: Try[Int] = Try {
        throw new IllegalStateException("We were not ready for this...")
      }

      def service(): Future[Int] = Future.fromTry {
        dependency.map { d =>
          d / 2
        }
      }

      val resultFuture: Future[Int] = service()
      Await.ready(resultFuture, Duration(1, MINUTES))
      def getValue = Await.result(resultFuture, Duration(1, MINUTES))
      getValue must throwA[IllegalStateException]("We were not ready for this...")
    }

    "success with multiple dependencies" in {

      lazy val dependency1: Try[Int] = Try {
        82
      }

      lazy val dependency2: Try[Int] = Try {
        86
      }

      lazy val dependency3: Try[Int] = Try {
        84
      }

      def service(): Future[Int] = {
        Future.fromTry(
          for {
            d1 <- dependency1
            d2 <- dependency2
            d3 <- dependency3
          } yield (d1 + d2 + (d3 * 2)) / 8
        )
      }

      val resultFuture: Future[Int] = service()
      val resultValue = Await.result(resultFuture, Duration(1, MINUTES))

      resultValue must be equalTo 42
    }

    "fail if one of the multiple dependencies (Try) fails" in {

      lazy val dependency1: Try[Int] = Try {
        82
      }

      lazy val dependency2: Try[Int] = Try {
        throw new IllegalStateException("Ach Mensch...")
      }

      lazy val dependency3: Try[Int] = Try {
        84
      }

      def service(): Future[Int] = {
        Future.fromTry(
          for {
            d1 <- dependency1
            d2 <- dependency2
            d3 <- dependency3
          } yield (d1 + d2 + (d3 * 2)) / 8
        )
      }

      val resultFuture: Future[Int] = service()
      Await.ready(resultFuture, Duration(1, MINUTES))
      def getValue = Await.result(resultFuture, Duration(1, MINUTES))
      getValue must throwA[IllegalStateException]("Ach Mensch...")
    }

    "fail if one of the multiple dependencies (Try) fails and do not test following dependencies" in {

      var dependenciesTested = 0

      lazy val dependency1: Try[Int] = Try {
        dependenciesTested += 1
        82
      }

      lazy val dependency2: Try[Int] =  {
        dependenciesTested += 1
        Failure(new IllegalStateException("Blabla"))
      }

      lazy val dependency3: Try[Int] = Try {
        dependenciesTested += 1
        84
      }

      def service(): Future[Int] = {
        Future.fromTry(
          for {
            d1 <- dependency1
            d2 <- dependency2
            d3 <- dependency3
          } yield (d1 + d2 + (d3 * 2)) / 8
        )
      }

      val resultFuture: Future[Int] = service()
      Await.ready(resultFuture, Duration(1, MINUTES))
      def getValue = Await.result(resultFuture, Duration(1, MINUTES))
      getValue must throwA[IllegalStateException]("Blabla")

      dependenciesTested must be equalTo 2
    }
  }
}
