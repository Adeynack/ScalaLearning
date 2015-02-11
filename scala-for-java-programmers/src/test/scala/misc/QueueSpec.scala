package misc

import org.specs2.mutable.Specification

import scala.collection.immutable.Queue
import scala.collection.mutable

class QueueSpec extends Specification {
  sequential

  "queue" should {

    "be accessible from head or tail" in {
      var q = Queue[Int](1, 2, 3, 4, 5, 6)
      q.head must be equalTo 1
      q.last must be equalTo 6

      val (value1, newQ1) = q.dequeue
      q = newQ1

      value1 must be equalTo 1
      q.head must be equalTo 2
      q.last must be equalTo 6

      val (value2, newQ2) = q.dequeue
      q = newQ2

      value2 must be equalTo 2
      q.head must be equalTo 3
      q.last must be equalTo 6
    }

    "be accessible from head or tail" in {
      val q = mutable.Queue[Int](1, 2, 3, 4, 5, 6)
      q.head must be equalTo 1
      q.last must be equalTo 6

      var value = q dequeue()

      value must be equalTo 1
      q.head must be equalTo 2
      q.last must be equalTo 6

      value = q.dequeue()

      value must be equalTo 2
      q.head must be equalTo 3
      q.last must be equalTo 6
    }

    "be purged using 'dropWhile' (immutable version)" in {

      println("--== Immutable version ==--")

      var q = Queue[Int](1, 2, 3, 4, 5, 6)
      q must contain(exactly(1, 2, 3, 4, 5, 6)).inOrder

      def clean(limit: Int): Queue[Int] =
        q.dropWhile({ i =>
          val result = i < limit
          println(s"Comparing (i = $i) < (limit = $limit). Result: $result")
          result
        })

      println("-- Cleaning 3")
      q = clean(3)
      q must contain(exactly(3, 4, 5, 6)).inOrder

      q = q :+ 7
      q must contain(exactly(3, 4, 5, 6, 7)).inOrder

      q = q :+ 8
      q must contain(exactly(3, 4, 5, 6, 7, 8)).inOrder

      println("-- Cleaning 8")
      q = clean(8)
      q must contain(exactly(8)).inOrder

      q = 2 +: q
      q must contain(exactly(2, 8)).inOrder

      println("-- Cleaning 8")
      q = clean(8)
      q must contain(exactly(8)).inOrder
    }

    "be purged using dropWhile (mutable queue)" in {

      println("--== Mutable version ==--")

      val q = mutable.Queue[Int](1, 2, 3, 4, 5, 6)
      q must contain(exactly(1, 2, 3, 4, 5, 6)).inOrder

      def clean(limit: Int): Unit =
        q.dequeueAll({ i =>
          val result = i < limit
          println(s"MUT: Comparing (i = $i) < (limit = $limit). Result: $result")
          result
        })

      println("MUT: -- Cleaning 3")
      clean(3)
      q must contain(exactly(3, 4, 5, 6)).inOrder

      q enqueue 7
      q must contain(exactly(3, 4, 5, 6, 7)).inOrder

      q enqueue 8
      q must contain(exactly(3, 4, 5, 6, 7, 8)).inOrder

      println("MUT: -- Cleaning 8")
      clean(8)
      q must contain(exactly(8)).inOrder
    }

    "be purged using manual function (mutable queue)" in {

      println("--== Mutable version ==--")

      val q = mutable.Queue[Int](1, 2, 3, 4, 5, 6)
      q must contain(exactly(1, 2, 3, 4, 5, 6)).inOrder

      //      def clean(limit: Int): Unit = {
      //        var r: Option[Int] = None
      //        do {
      //          r = q.dequeueFirst({ i =>
      //            val result = i < limit
      //            println(s"MUT/MAN: Comparing (i = $i) < (limit = $limit). Result: $result")
      //            result
      //          })
      //          println(s"MUT/MAN: Dequeued ${r.getOrElse("None")}")
      //        } while (r.isDefined)
      //      }

      def clean(limit: Int): Unit = {
        def predicate(i: Int) = {
          val result = i < limit
          println(s"MUT/MAN: Comparing (i = $i) < (limit = $limit). Result: $result")
          result
        }
        while (predicate(q.head)) {
          q.dequeue()
        }
      }

      println("MUT/MAN: -- Cleaning 3")
      clean(3)
      q must contain(exactly(3, 4, 5, 6)).inOrder

      q enqueue 7
      q must contain(exactly(3, 4, 5, 6, 7)).inOrder

      q enqueue 8
      q must contain(exactly(3, 4, 5, 6, 7, 8)).inOrder

      println("MUT/MAN: -- Cleaning 8")
      clean(8)
      q must contain(exactly(8)).inOrder
    }


  }
}
