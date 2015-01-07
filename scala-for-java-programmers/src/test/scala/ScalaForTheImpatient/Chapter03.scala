/*
 *
 *    Scala for the Impatient
 *
 *    Chapter 3 : Working with Arrays
 *
 */
package ScalaForTheImpatient

import org.specs2.mutable.Specification

import scala.collection.mutable.ArrayBuffer

object Chapter03 extends Specification {

  /*
      3.1 Fixed-Length Arrays
   */

  "fixed size arrays (of int)" should {

    "be created with type and size" in {
      val nums = new Array[Int](5)
      nums must have length 5
      nums must be equalTo Array(0, 0, 0, 0, 0)
      nums(2) = 42
      nums must be equalTo Array(0, 0, 42, 0, 0)
    }
  }

  "fixed size arrays (of string)" should {
    "be created with initial enumeration of elements" in {
      val s: Array[String] = Array("Hello", "world")

      s must have length 2
      s(0) must be equalTo "Hello"
      s(1) must be equalTo "world"

      s(0) = "Goodbye"
      s(0) must be equalTo "Goodbye"
      s(1) must be equalTo "world"
    }
  }

  /*
      3.2 Variable-Length Arrays: Array Buffers

      Equivalent to ArrayList (Java) or vector (C++).
   */

  "array buffers" should {

    "be created empty" in {
      val b = ArrayBuffer[Int]()
      b must not beNull

      b must have length 0
    }

    "add an element at the end" in {
      val b = ArrayBuffer[Int]()
      b must beEmpty
      b += 42
      b must have length 1
      b must contain(exactly(42)).inOrder
    }

    "add multiple elements at the end" in {
      val b = ArrayBuffer[Int](42)
      b +=(4, 5, 6, 7)
      b must have length 5
      b must contain(exactly(42, 4, 5, 6, 7)).inOrder
    }

    "add element from another collection" in {
      val b = ArrayBuffer[Int](48)
      b must have length 1

      val c = Array(51, 52, 53)

      b ++= c

      b must have length 4
      b must contain(exactly(48, 51, 52, 53)).inOrder
    }

    "trim elements at the end" in {
      val b = ArrayBuffer[Int](42, 4, 5, 6, 7, 8, 9, 58)
      b.trimEnd(6)
      b must have length 2
      b must contain(exactly(42, 4)).inOrder
    }

    "trim elements from the beginning" in {
      val b = ArrayBuffer(1, 2, 3, 4, 5, 6, 7, 8, 9)
      b must have length 9
      b.trimStart(7)
      b must have length 2
      b must contain(exactly(8, 9)).inOrder
    }

    "insert elements" in {
      val b = ArrayBuffer(10, 20, 30)
      b must contain(exactly(10, 20, 30)).inOrder
      b.insert(2, 25)
      b must contain(exactly(10, 20, 25, 30)).inOrder
    }

    "insert multiple elements" in {
      val b = ArrayBuffer(10, 20, 30)
      b must contain(exactly(10, 20, 30)).inOrder
      b.insert(2, 24, 26, 28)
      b must contain(exactly(10, 20, 24, 26, 28, 30)).inOrder
    }

    "remove elements" in {
      val b = ArrayBuffer(10, 20, 30)
      b must contain(exactly(10, 20, 30)).inOrder
      b.remove(1)
      b must contain(exactly(10, 30)).inOrder
    }

    "remove multiple elements" in {
      val b = ArrayBuffer(10, 20, 30, 40)
      b must contain(exactly(10, 20, 30, 40)).inOrder
      b.remove(1, 3)
      b must contain(exactly(10)).inOrder
    }

    "can convert to array" in {
      val b = ArrayBuffer(10, 20)
      val a = b.toArray
      a must beAnInstanceOf[Array[Int]]
      a must have length 2
      a(0) must be equalTo 10
      a(1) must be equalTo 20
    }

    "be converted from an array" in {
      val a = Array('x', 'y', 'z')
      val b = a.toBuffer
      b must beAnInstanceOf[ArrayBuffer[Char]]
      b must contain(exactly('x', 'y', 'z')).inOrder
    }
  }

  /*
      3.3 Traversing Arrays and Array Buffers
   */

  "traversing arrays" should {

    "be possible using the length" in {
      val a = Array('a', 'b', 'c', 'x', 'y', 'z')
      val sb = new StringBuilder
      for (i <- 0 until a.length) {
        sb append a(i)
      }
      sb toString() must be equalTo "abcxyz"
    }

    "be possible using the length, but skipping even elements" in {
      val a = Array('a', 'b', 'c', 'x', 'y', 'z')
      val sb = new StringBuilder
      for (i <- 0 until(a.length, 2)) {
        sb append a(i)
      }
      sb toString() must be equalTo "acy"
    }

    "be possible using the length and reversed" in {
      val a = Array('a', 'b', 'c', 'x', 'y', 'z')
      val sb = new StringBuilder
      for (i <- (0 until a.length).reverse) {
        sb append a(i)
      }
      sb toString() must be equalTo "zyxcba"
    }

    "be possible using the range syntax" in {
      val a = Array(1, 2, 3, 7, 8, 9)
      val sb = new StringBuilder
      for (e <- a) {
        sb append e
      }
      sb toString() must be equalTo "123789"
    }
  }

  /*
      3.4 Transforming Arrays
   */

  "transforming arrays" should {

    "be done using list comprehension, yielding a new array" in {
      val a = Array(2, 3, 5, 7, 11)
      val result = for (e <- a) yield 2 * e
      result must beAnInstanceOf[Array[Int]]
      result must have length 5
      result must be equalTo Array(4, 6, 10, 14, 22)
    }

    "be done using list comprehension with guard for selecting only specific elements, then yielding a new array" in {
      val a = Array(2, 3, 5, 7, 11)
      // select elements from a in e, but only if they are odd (e % 2 == 0), then yield 3 times e in a new array.
      val result = for (e <- a if e % 2 == 0) yield 3 * e
      result must beAnInstanceOf[Array[Int]]
      result must have length 1
      result(0) must be equalTo 6

      // It does not modify the original array.
      a must beAnInstanceOf[Array[Int]]
      a must have length 5
      a must be equalTo Array(2, 3, 5, 7, 11)
    }

    // Different ways of writing 'list comprehension':
    //
    // for (e <- a if e % 2 == 0          )   yield 3 * e
    // a           filter (e => e % 2 == 0)   map (e => 3 * e)
    // a           filter (_ % 2 == 0)        map (3 * _)
    //

    "be done with the explicit method calls" in {
      val a = Array(2, 3, 5, 7, 11)
      // select elements from a in e, but only if they are odd (e % 2 == 0), then yield 3 times e in a new array.
      val result = a filter (_ % 2 == 0) map (3 * _)
      result must beAnInstanceOf[Array[Int]]
      result must be equalTo Array(6)
    }

    // todo: The code in the next block comes from the book, but does not even do the same thing. Commented out for the moment.

    //    "be used for removing all but the first negative number (use case example)" in {
    //
    //      def example: ArrayBuffer[Int] = ArrayBuffer(4, -1, 2 - 4, -3, 6)
    //
    //      "use sequential version" in {
    //        val a = example
    //
    //        var first = true
    //        var n = a.length
    //        var i = 0
    //        while (i < n) {
    //          if (a(i) >= 0) i += 1
    //          else {
    //            if (first) {
    //              first = false; i += 1
    //            }
    //            else {
    //              a.remove(i); n -= 1
    //            }
    //          }
    //        }
    //
    //        a must contain(exactly(4, -1, 2, 6))
    //      }
    //
    //      "use list comprehension version" in {
    //        val a = example
    //
    //        var first = false
    //        val indexes = for (i <- 0 until a.length if first || a(i) >= 0) yield {
    //          if (a(i) < 0) first = false; i
    //        }
    //        for (j <- 0 until indexes.length) a(j) = a(indexes(j))
    //        a.trimEnd(a.length - indexes.length)
    //
    //        a must contain(exactly(4, -1, 2, 6))
    //      }
    //    }
  }

  /*
      3.5 Common Algorithms
   */

  "common algorithms" should {

    "calculate sum" in {
      Array(1, 2, 3, 4).sum must be equalTo 10
      ArrayBuffer(1, 2, 3, 4).sum must be equalTo 10
    }

    "identify the maximum numeric value" in {
      val a = Array(BigInt("4325768597689547897"), BigInt("32758943276890574896754380768953478960"), BigInt("-42378954739860754890276895437890"))
      val expected = a(1)
      a.max must be equalTo expected
    }

    "identify the maximum text value" in {
      val a = Array("Mary", "had", "a", "little", "lamb")
      a.max must be equalTo "little"
      a.min must be equalTo "Mary"
    }

    "order arrays" in {
      val a = ArrayBuffer(2, 3, 1)
      a must contain(exactly(2, 3, 1)).inOrder
      val s = a.sorted
      s must contain(exactly(1, 2, 3)).inOrder
    }

    "order arrays with specific sort method" in {
      val a = ArrayBuffer(0.2, 1.0, 2.6, 3.1)
      a must contain(exactly(0.2, 1.0, 2.6, 3.1)).inOrder

      val s = a.sortWith(math.sin(_) < math.sin(_))

      s must contain(exactly(3.1, 0.2, 2.6, 1.0)).inOrder
    }

    "order arrays with specific sort method (named method)" in {
      val a = ArrayBuffer(2, 10, 25, 30)
      a must contain(exactly(2, 10, 25, 30)).inOrder

      def sorter(a: Int, b: Int): Boolean = {
        math.sin(a / 10.0) < math.sin(b / 10.0)
      }

      val s = a.sortWith(sorter)

      s must contain(exactly(30, 2, 25, 10)).inOrder
    }

    "order arrays with specific sort method (with degrees and named anonymous function parameters)" in {
      val a = ArrayBuffer(15, 60, 150, 180)
      a must contain(exactly(15, 60, 150, 180)).inOrder

      // NB: when more than one transformation is done, the parameters apparently need to be named.
      val s = a.sortWith((a: Int, b: Int) => math.sin(math.toRadians(a)) < math.sin(math.toRadians(b)))

      s must contain(exactly(180, 15, 150, 60)).inOrder
    }

    "order arrays (but not ArrayBuffers) in place" in {
      val a = Array(1, 7, 2, 9)
      scala.util.Sorting.quickSort(a)
      a must be equalTo Array(1, 2, 7, 9)
    }

    // todo: Make this home-made example work.
    //
    // diverging implicit expansion for type DegreeSinus => Comparable[DegreeSinus]
    //      [error] starting with method $conforms in object Predef
    //      [error]       scala.util.Sorting.quickSort(a)
    //      [error]                                   ^
    //
//        "order arrays of elements extending trait 'Ordered'" in {
    //
    //          class DegreeSinus(deg: Double) extends Ordered[Double] {
    //            def degrees = deg
    //            override def compare(that: Double): Int = math.sin(this.deg).compare(math.sin(that))
    //            def printToConsole = println(this.degrees)
    //          }
    //
    //          object DegreeSinus {
    //
    //            def apply(deg: Double) : DegreeSinus = new DegreeSinus(deg)
    //            def apply(deg: Int) : DegreeSinus = new DegreeSinus(deg)
    //
    //            implicit def fromDouble(deg: Double) : DegreeSinus = new DegreeSinus(deg)
    //            implicit def fromInt(deg: Int) : DegreeSinus = new DegreeSinus(deg)
    //
    //            implicit def toDouble(ds: DegreeSinus): Double = ds.degrees
    //            implicit def toInt(ds: DegreeSinus): Int = ds.degrees.asInstanceOf[Int]
    //          }
    //
    //          val a = Array[DegreeSinus](15, 60.5123, 150, 180)
    //          scala.util.Sorting.quickSort(a)
    //
    ////          DegreeSinus(100) printToConsole
    //
    //          a must be equalTo Array(180, 15, 150, 60.5123)
    //        }

    "be exportable into readable strings" in {
      val a = Array(1, 2, 7, 9)
      a.mkString(" and ") must be equalTo "1 and 2 and 7 and 9"
      a.mkString("<", ",", ">") must be equalTo "<1,2,7,9>"
    }
  }
}