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
      "have size of 10" in {
        nums must have length 5
      }
      "all element equal 0" ! nums.forall(e => e == 0)
      "be modifiable" in {
        nums(2) = 42
        nums(0) must be equalTo 0
        nums(1) must be equalTo 0
        nums(2) must be equalTo 42
        nums(3) must be equalTo 0
        nums(4) must be equalTo 0
      }
    }
  }

  "fixed size arrays (of string)" should {
    "be created with initial enumeration of elements" in {
      val s: Array[String] = Array("Hello", "world")

      s must have length 2
      s(0) must be equalTo "Hello"
      s(1) must be equalTo "world"

      "and be modifiable" in {
        s(0) = "Goodbye"
        s(0) must be equalTo "Goodbye"
        s(1) must be equalTo "world"
      }
    }
  }

  /*
      3.2 Variable-Length Arrays: Array Buffers

      Equivalent to ArrayList (Java) or vector (C++)
   */

  "array buffers" should {

    "be created empty" in {
      val b = ArrayBuffer[Int]()
      b must not beNull;
      b.length must be equalTo 0
    }

    "add an element at the end" in {
      val b = ArrayBuffer[Int]()
      b += 42
      b.length must be equalTo 1
    b(0) must be equalTo 42
  }

  "add multiple elements at the end" in {
    val b = ArrayBuffer[Int](42)
    b +=(4, 5, 6, 7)
    b.length must be equalTo 5
    b(0) must be equalTo 42
    b(1) must be equalTo 4
    b(2) must be equalTo 5
    b(3) must be equalTo 6
    b(4) must be equalTo 7
  }

  "add element from another collection" in {
    val b = ArrayBuffer[Int](48)
    b.length must be equalTo 1

    val c = Array(51, 52, 53)

    b ++= c

    b.length must be equalTo 4
    b(0) must be equalTo 48
    b(1) must be equalTo 51
    b(2) must be equalTo 52
    b(3) must be equalTo 53
  }

    "trim elements at the end" in {
      val b = ArrayBuffer[Int](42, 4, 5, 6, 7, 8, 9, 58)
      b.trimEnd(6)
      b.length must be equalTo 2
      b(0) must be equalTo 42
      b(1) must be equalTo 4
    }

    "trim elements from the begining" in {
      val b = ArrayBuffer(1, 2, 3, 4, 5, 6, 7, 8, 9)
      b.length must be equalTo 9
      b.trimStart(7)
      b.length must be equalTo 2
      b(0) must be equalTo 8
      b(1) must be equalTo 9
    }
  }
}