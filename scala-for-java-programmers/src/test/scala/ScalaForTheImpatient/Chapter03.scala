/*
 *
 *    Scala for the Impatient
 *
 *    Chapter 3 : Working with Arrays
 *
 */
package ScalaForTheImpatient

import org.specs2.mutable.Specification
import util.AutoIncrementInt

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Sorting

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
      Sorting quickSort a
      a must be equalTo Array(1, 2, 7, 9)
    }

    // todo: Make this home-made example work.
    //
    // diverging implicit expansion for type DegreeSinus => Comparable[DegreeSinus]
    //      [error] starting with method $conforms in object Predef
    //      [error]       Sorting quickSort a
    //      [error]                         ^
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
    //          Sorting quickSort a
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

  /*
      3.6 Deciphering Scaladoc (additional methods)
   */

  "additional methods" should {

    "append" in {
      val b = ArrayBuffer(1, 2, 3)
      b should contain(exactly(1, 2, 3)).inOrder
      b.append(34, 35, 36)
      b should contain(exactly(1, 2, 3, 34, 35, 36)).inOrder
    }

    "appendAll" in {
      val s = Seq(11, 12, 13)
      val b = ArrayBuffer(1, 2, 3)
      b should contain(exactly(1, 2, 3)).inOrder
      b.appendAll(s)
      b should contain(exactly(1, 2, 3, 11, 12, 13)).inOrder
    }

    "count" in {
      Seq(1, 2, 3, 4, 5).count(_ % 2 == 0) must be equalTo 2

      Array(1, 2, 3).count(_ > 1) must be equalTo 2

      ArrayBuffer(1, 2, 3, 4, 5, 6, 7, 8).count(e => {
        if (e < 5) {
          e % 2 == 1 // 1, 3
        } else {
          e % 4 == 0 // 8
        }
      }) must be equalTo 3

    }

    "+= and -=" in {
      val b = ArrayBuffer(1, 2, 3)
      b must contain(exactly(1, 2, 3)).inOrder

      b += 8
      b must contain(exactly(1, 2, 3, 8)).inOrder

      b -= 2
      b must contain(exactly(1, 3, 8)).inOrder

      b += 42 -= 1 += 96 -= 3
      b must contain(exactly(8, 42, 96)).inOrder
    }

    "copyToArray" in {
      val a = new Array[Int](10)
      a must be equalTo Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

      val b = ArrayBuffer(1, 2, 3)
      b must contain(exactly(1, 2, 3)).inOrder

      b copyToArray a
      a must be equalTo Array(1, 2, 3, 0, 0, 0, 0, 0, 0, 0)

      val b2 = Array(4, 5, 6)
      b2.copyToArray(a, 3)
      a must be equalTo Array(1, 2, 3, 4, 5, 6, 0, 0, 0, 0)
    }

    "max" in {
      object SinOrdering extends Ordering[Int] {
        override def compare(a: Int, b: Int): Int = {
          val sinA: Double = math.sin(math.toRadians(a))
          val sinB: Double = math.sin(math.toRadians(b))
          sinA.compare(sinB)
        }
      }
      val a = Array(15, 60, 150, 180)

      // with natural ordering
      a.max must be equalTo 180

      // with sinus to sinus ordering
      a.max(SinOrdering) must be equalTo 60
    }
  }

  /*
      3.7 Multidimensional Arrays
   */

  "multidimensional arrays" should {

    def linearFill(arr: Array[Array[Int]]) {
      val v = new AutoIncrementInt
      for (r <- 0 until arr.length; c <- 0 until arr(r).length) arr(r)(c) = v next
    }

    "be built using 'ofDim' syntax (matrix)" in {
      val matrix = Array.ofDim[Int](2, 3) // 3 rows, 4 columns
      linearFill(matrix)
      matrix(0)(0) must be equalTo 0
      matrix(0)(1) must be equalTo 1
      matrix(0)(2) must be equalTo 2
      matrix(1)(0) must be equalTo 3
      matrix(1)(1) must be equalTo 4
      matrix(1)(2) must be equalTo 5
    }

    "be built in a 'ragged arrays' form (not the same length for 2nd dimension arrays)" in {
      val triangle = new Array[Array[Int]](4)
      for (i <- 0 until triangle.length) triangle(i) = new Array[Int](i + 1)
      linearFill(triangle)
      triangle(0) must be equalTo Array(0)
      triangle(1) must be equalTo Array(1, 2)
      triangle(2) must be equalTo Array(3, 4, 5)
      triangle(3) must be equalTo Array(6, 7, 8, 9)
    }
  }

  /*
      3.8 Interoperating with Java
   */

  "interoperating with Java" should {

    "allow to use Scala ArrayBuffer as a Java List" in {
      import scala.collection.JavaConversions.bufferAsJavaList
      val buffer = ArrayBuffer(1, 2, 3)
      val javaList: java.util.List[Int] = buffer // Scala to Java
      javaList must not beNull;
      javaList.get(0) must be equalTo 1
      javaList.get(1) must be equalTo 2
      javaList.get(2) must be equalTo 3
    }

    "allow to use Java List as Scala Buffer" in {
      import scala.collection.JavaConversions.asScalaBuffer
      val javaList = new java.util.ArrayList[Int]()
      javaList.add(1)
      javaList.add(2)
      javaList.add(3)
      val buffer: mutable.Buffer[Int] = javaList // Java to Scala
      buffer must contain(exactly(1, 2, 3)).inOrder
    }

    "allow both way (wrapping and unwrapping) conversion" in {
      import scala.collection.JavaConversions.bufferAsJavaList
      import scala.collection.JavaConversions.asScalaBuffer
      val buffer = ArrayBuffer(1, 2, 3)
      val javaList: java.util.List[Int] = buffer // Scala to Java
      val unwrappedBuffer: mutable.Buffer[Int] = javaList // Java to Scala, simply unwrapping.
      unwrappedBuffer must contain(exactly(1, 2, 3)).inOrder
    }
  }

  /*
      End of chapter exercises.
   */

  "exercises" should {

    "1. Write a code snippet that sets 'a' to an array of 'n' random integers between [0, n[." in {
      def set(array: Array[Int], value: Int): Unit = {
        for (index <- 0 until array.length) {
          array(index) = value
        }
      }
      val a = Array(1, 2, 3, 4)
      set(a, 42)
      a must be equalTo Array(42, 42, 42, 42)
    }

    "2. Write a loop that swaps adjacent elements of an array of integers. For example, Array(1, 2, 3, 4, 5) becomes Array(2, 1, 4, 3, 5)." in {
      def swapAdjacent(array: Array[Int]): Array[Int] = {
        var i = 0
        while (i < array.length - 1) {
          val v1 = array(i)
          val v2 = array(i + 1)
          if (v1 + 1 == v2) {
            array(i) = v2
            array(i + 1) = v1
            i += 2
          } else {
            i += 1
          }
        }
        array
      }

      swapAdjacent(Array(1, 3, 5, 7)) must be equalTo Array(1, 3, 5, 7)
      swapAdjacent(Array(1, 2, 3, 7)) must be equalTo Array(2, 1, 3, 7)
      swapAdjacent(Array(1, 2, 3, 4, 5)) must be equalTo Array(2, 1, 4, 3, 5)
    }

    "3. Repeat the preceding assignment, but produce a new array with the swapped values. Use for/yield." in {
      def swapAdjacent(array: Array[Int]): Array[Int] = {
        var swapping: Option[Int] = None
        val result =
          for (index <- 0 until array.length) yield {
            if (swapping.isDefined) {
              val s = swapping.get
              swapping = None
              s
            } else {
              val v1 = array(index)
              if (index == array.length - 1) {
                v1
              } else {
                val v2 = array(index + 1)
                if (v1 + 1 == v2) {
                  swapping = Some(v1)
                  v2
                } else {
                  v1
                }
              }
            }
          }
        result.toArray
      }

      swapAdjacent(Array(1, 3, 5, 7)) must be equalTo Array(1, 3, 5, 7)
      swapAdjacent(Array(1, 2, 3, 7)) must be equalTo Array(2, 1, 3, 7)
      swapAdjacent(Array(1, 2, 3, 4, 5)) must be equalTo Array(2, 1, 4, 3, 5)
    }

    "3.1. Alternative understanding of the question" in {
      def swapAdjacent(array: Array[Int]): Array[Int] = {
        val maxIndexToCheck: Int = array.length - 1
        val result =
          for (index <- 0 until array.length) yield {
            if (index % 2 == 0) {
              if (index < maxIndexToCheck) {
                array(index + 1)
              } else {
                array(index)
              }
            } else {
              array(index - 1)
            }
          }
        result.toArray
      }

      swapAdjacent(Array(1, 3, 5, 7)) must be equalTo Array(3, 1, 7, 5)
      swapAdjacent(Array(1, 2, 3, 7)) must be equalTo Array(2, 1, 7, 3)
      swapAdjacent(Array(1, 2, 3, 4, 5)) must be equalTo Array(2, 1, 4, 3, 5)
    }

    "4. Given an array of integers, produce a new array that contains all positive\nvalues of the original array, in their original order, followed by all values that\nare zero or negative, in their original order." in {
      def organize(array: Array[Int]): Array[Int] = {
        val result = new ArrayBuffer[Int]
        result ++= (for (e <- array if e > 0) yield e) ++= (for (e <- array if e <= 0) yield e)
        result toArray
      }

      organize(Array(1, 2, -3, 4, -5)) must be equalTo Array(1, 2, 4, -3, -5)
      organize(Array(1, 2, 3, 4, 5)) must be equalTo Array(1, 2, 3, 4, 5)
      organize(Array(-1, -2, -3, -4, 5)) must be equalTo Array(5, -1, -2, -3, -4)
      organize(Array(1, 2, 0, 4, -5)) must be equalTo Array(1, 2, 4, 0, -5)
    }

    "5. How do you compute the average of an Array[Double]?" in {
      def average(numbers: Array[Double]): Double = numbers.sum / numbers.length
      average(Array(1, 2, 3)) must be equalTo 2.0
      average(Array(88, 89, 92, 92, 87, 92)) must be equalTo 90.0
    }

    "6. a) How do you rearrange the elements of an Array[Int] so that they appear in\nreverse sorted order? How do you do the same with an ArrayBuffer[Int]?" in {
      def reverse[T](a: Array[T]): Array[T] = {
        val midIndex = if (a.length % 2 == 0) a.length / 2 else (a.length - 1) / 2
        for (index <- 0 until midIndex) {
          val reverseIndex = a.length - 1 - index
          val reverseValue = a(index)
          a(index) = a(reverseIndex)
          a(reverseIndex) = reverseValue
        }
        a
      }

      reverse(Array(1, 2, 3, 4)) must be equalTo Array(4, 3, 2, 1)
      reverse(Array('a', 'b', 'c')) must be equalTo Array('c', 'b', 'a')
    }

    "6. b) The same with an ArrayBuffer" in {

      //      def reverse[T <: Ordered[T]](a: ArrayBuffer[T]) : ArrayBuffer[T] = {
      //        a.sortWith((a: T, b: T) => a > b)
      //      }
      //      reverse(ArrayBuffer(1, 2, 3, 4)) must contain(exactly(4, 3, 2, 1)).inOrder
      //      reverse(ArrayBuffer('a', 'b', 'c')) must contain(exactly('c', 'b', 'a')).inOrder

      ArrayBuffer(1, 2, 3, 4).sortWith(_ > _) must contain(exactly(4, 3, 2, 1)).inOrder
      ArrayBuffer('a', 'b', 'c').sortWith(_ > _) must contain(exactly('c', 'b', 'a')).inOrder
    }

    "7. Write a code snippet that produces all values from an array with duplicates\nremoved. (Hint: Look at Scaladoc.)" in {
      def removeDuplicates[T](a: Array[T]): Array[T] = {
        a distinct
      }

      removeDuplicates(Array(1, 2, 1, 3, 1)) must be equalTo Array(1, 2, 3)
      removeDuplicates(Array(4, 7, 4, 2, 4, 65, 7, 8, 78, 6, 4, 32)) must be equalTo Array(4, 7, 2, 65, 8, 78, 6, 32)
    }

    "8. Rewrite the example at the end of Section 3.4, “Transforming Arrays,” on\npage 32. Collect indexes of the negative elements, reverse the sequence, drop\nthe last index, and call a.remove(i) for each index. Compare the efficiency of\nthis approach with the two approaches in Section 3.4." in {
      true must beTrue
      // todo: The example in question was not 'working' (see todo comment in section 3.4).
    }

    "9. Make a collection of all time zones returned by java.util.TimeZone.getAvailableIDs\nthat are in America. Strip off the \"America/\" prefix and sort the result." in {
      val americaTimeZonePrefix = "America/"

      // Using list comprehension.
      val timeZonesInAmericaFromListComprehension =
        for (z <- java.util.TimeZone.getAvailableIDs
              if z.startsWith(americaTimeZonePrefix))
        yield z substring americaTimeZonePrefix.length
      Sorting quickSort timeZonesInAmericaFromListComprehension

      // Using method chaining.
      val timeZonesInAmericaFromMethodCalls =
        java.util.TimeZone.getAvailableIDs
          .filter(_ startsWith americaTimeZonePrefix)
          .map(_ substring americaTimeZonePrefix.length)
          .sorted

      timeZonesInAmericaFromListComprehension must be equalTo timeZonesInAmericaFromMethodCalls
    }

    "10. Import java.awt.datatransfer._ and make an object of type SystemFlavorMap with\nthe call\nval flavors = SystemFlavorMap.getDefaultFlavorMap().asInstanceOf[SystemFlavorMap]\nThen call the getNativesForFlavor method with parameter DataFlavor.imageFlavor\nand get the return value as a Scala buffer. (Why this obscure class? It’s hard\nto find uses of java.util.List in the standard Java library.)" in {
      import java.awt.datatransfer._
      import scala.collection.JavaConversions.asScalaBuffer
      val flavors = SystemFlavorMap.getDefaultFlavorMap.asInstanceOf[SystemFlavorMap]
      val natives : mutable.Buffer[String] = flavors.getNativesForFlavor(DataFlavor.imageFlavor)
      natives must contain(exactly("PNG", "JFIF", "TIFF")).inOrder
    }
  }
}
