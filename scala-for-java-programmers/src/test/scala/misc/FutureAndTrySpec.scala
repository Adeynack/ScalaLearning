package misc

import java.util.concurrent.TimeUnit._

import org.specs2.mutable.Specification

import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Try}

class FutureAndTrySpec extends Specification {
  sequential

  def toFuture[T](o: Option[T]): Future[T] = {
    o match {
      case None => throw new NoSuchElementException("No value.")
      case Some(value) => Future.successful(value)
    }
  }

  def awaitResult[T](f: Future[T]): T = Await.result(f, Duration(10, SECONDS))

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
      val resultValue = awaitResult(resultFuture)

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

      def getValue = awaitResult(resultFuture)

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
      def getValue = awaitResult(resultFuture)
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
      val resultValue = awaitResult(resultFuture)

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
      def getValue = awaitResult(resultFuture)
      getValue must throwA[IllegalStateException]("Ach Mensch...")
    }

    "fail if one of the multiple dependencies (Try) fails and do not test following dependencies" in {

      var dependenciesTested = 0

      lazy val dependency1: Try[Int] = Try {
        dependenciesTested += 1
        82
      }

      lazy val dependency2: Try[Int] = {
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
      def getValue = awaitResult(resultFuture)
      getValue must throwA[IllegalStateException]("Blabla")

      dependenciesTested must be equalTo 2
    }
  }

  "Chains of futures" should {

    case class Value[T](value: T, id: String)

    class Service() {
      def listOfParentElements: Seq[Value[Int]] =
        for (i <- 1 to 3) yield Value(i, i.toString)

      def listOfSecondElements(parentElementId: String): Seq[Value[String]] =
        for (i <- 1 to 5) yield Value(s"Second #$i for parent $parentElementId.", s"s$i")

      def somethingWithFirstAndSecondId(firstId: String, secondId: String): Seq[Value[String]] =
        for (i <- 1 to 10) yield Value(s"Something with '$firstId' and '$secondId'.", s"f $firstId of $secondId")
    }

    val service = new Service()

    def use[TReturn](action: Service => TReturn): Future[TReturn] = {
      val result: TReturn = action(service)
      Future.successful(result)
    }

    "work with some 1-generic converter" in {
      sequential

      def firstOf[T](seq: Seq[T]): Future[T] =
        seq.headOption match {
          case Some(value) => Future.successful(value)
          case _ => Future.failed(new NoSuchElementException("No element found."))
        }

      val finalAnswerFuture: Future[String] =
        for (
          accounts <- use(_.listOfParentElements);
          firstOfFirstElement <- firstOf(accounts);
          firstOfFirstId = firstOfFirstElement.id;
          secondElements <- use(_.listOfSecondElements(firstOfFirstId));
          firstOfSecondElement <- firstOf(secondElements);
          firstOfSecondId = firstOfSecondElement.id;
          finalResults <- use(_.somethingWithFirstAndSecondId(firstOfFirstId, firstOfSecondId));
          firstOfFinalResult <- firstOf(finalResults)
        ) yield firstOfFinalResult.id

      val finalAnswer = awaitResult(finalAnswerFuture)
      finalAnswer must be equalTo "f 1 of s1"
    }

    "work with some 2-generics converter" in {

      def withFirst[TIn, TOut](seq: Seq[TIn], f: TIn => TOut): Future[TOut] =
        seq.headOption match {
          case Some(value) => Future.successful(f(value))
          case _ => Future.failed(new NoSuchElementException("No element found."))
        }

      // Simple example ... must explicit the type parameter...

      def firstList = Future.successful(Seq(1, 2, 3, 4, 5))

      val firstToString1: String = awaitResult(withFirst[Int, String](awaitResult(firstList), i => i.toString))

      val firstToString2: String =
        awaitResult(
          for {
            e <- firstList
            s <- withFirst[Int, String](e, i => i.toString)
          } yield s
        )

      firstToString1 must be equalTo "1"
      firstToString2 must be equalTo "1"

      // Complex example ... must explicit the type parameters :'(

      val finalAnswerFuture: Future[String] =
        for {
          accounts <- use(_.listOfParentElements)
          firstOfFirstId <- withFirst[Value[Int], String](accounts, _.id)
          secondElements <- use(_.listOfSecondElements(firstOfFirstId))
          firstOfSecondId <- withFirst[Value[String], String](secondElements, _.id)
          finalResults <- use(_.somethingWithFirstAndSecondId(firstOfFirstId, firstOfSecondId))
          finalId <- withFirst[Value[String], String](finalResults, _.id)
        } yield finalId

      val finalAnswer = Await.result(finalAnswerFuture, Duration(10, SECONDS))
      finalAnswer must be equalTo "f 1 of s1"
    }

//    "fails gracefully if a step fails" in {
//
//      def doSomething(i: Int): Future[Int] = {
//        val newValue = i * 2
//        if (newValue > 7) throw new Exception("This is too much!")
//        Future.successful(newValue)
//      }
//
//      val finalAnswerFuture: Future[Int] =
//        for {
//          a: Seq[Int] <- Future.successful(Seq(1,2,3,4,5))
//          b: Int <- a //Seq(1,2,3,4,5)
//          c: Int <- doSomething(b)
//        } yield c
//
//      val finalAnswer = Await.ready(finalAnswerFuture, Duration(10, SECONDS))
//      finalAnswer.
//    }


    //// Just a piece of code to understand 'Either'.
    //    "asd" in {
    //
    //      val e: Either[Int, Throwable] = Left(42)
    //      e match {
    //        case Left(i) => println(i)
    //        case Right(e: NullPointerException) => println("null")
    //        case Right(e: IllegalStateException) => println("illegal")
    //        case Right(e: Throwable) => println("whatever else...")
    //      }
    //
    //      true must beTrue
    //    }
  }
}
