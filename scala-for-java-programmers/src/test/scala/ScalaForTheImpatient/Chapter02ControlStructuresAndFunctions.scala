/*
 *
 *    Scala for the Impatient
 *
 *    Chapter 2 : Control Structures and Functions
 *
 */
package ScalaForTheImpatient

import java.text.MessageFormat
import java.util

import org.specs2.mutable.Specification

object Chapter02ControlStructuresAndFunctions extends Specification {

  "list comprehension" should {

    //    "have the right content" in {
    ////      val l: util.List[Integer] = new util.ArrayList[Integer]
    //      val l = new util.ArrayList[Integer]
    //      for (i <- 1 to 3; j <- 1 to 3) {
    //        l add (10 * i + j)
    //      }
    ////      l must containTheSameElementsAs(Seq(11, 12, 13, 21, 22, 23, 31, 32, 33))
    //      //  Seq(1, 2, 3, 4) must contain(allOf(be_>(0), be_>(1)).inOrder)
    //      val expected =  List(11, 12, 13, 21, 22, 23, 31, 32, 33)
    //      l must containsExactly(expected)
    //    }

    "work with dual enumeration" in {
      val b = StringBuilder.newBuilder
      for (i <- 1 to 3; j <- 1 to 3) {
        b append (10 * i + j)
        b append ' '
      }
      b.toString() must be equalTo "11 12 13 21 22 23 31 32 33 "
    }

    "work with a guard" in {
      val b = StringBuilder.newBuilder
      for (i <- 1 to 3; j <- 1 to 3 if i != j) {
        b append (10 * i + j)
        b append ' '
      }
      b.toString() must be equalTo "12 13 21 23 31 32 "
    }

    "work with definition" in {
      val b = StringBuilder.newBuilder
      for (i <- 1 to 3; from = 4 - i; j <- from to 3) {
        b append (10 * i + j)
        b append ' '
      }
      b.toString() must be equalTo "13 22 23 31 32 33 "
    }

    "works with yield" in {
      val v: Seq[Int] = for (i <- 1 to 10) yield i % 3
      v.length must be equalTo 10
      v(0) must be equalTo 1
      v(1) must be equalTo 2
      v(2) must be equalTo 0
      v(3) must be equalTo 1
      v(4) must be equalTo 2
      v(5) must be equalTo 0
      v(6) must be equalTo 1
      v(7) must be equalTo 2
      v(8) must be equalTo 0
      v(9) must be equalTo 1
    }

    "work with 'select' logic" in {
      val b = StringBuilder.newBuilder
      for (i <- 10 to(30, 10); j <- 1 to 3; r = "A%d".format(i + j)) {
        b append r
        b append ' '
      }
      b.toString() must be equalTo "A11 A12 A13 A21 A22 A23 A31 A32 A33 "
    }

    "work with 'select' logic with multiline syntax and guard" in {
      val b = StringBuilder.newBuilder
      for {i <- 10 to(30, 10) if (i/2) % 2 == 1
           j <- 1 to 3
           r = "A%d".format(i + j)} {
        b append r
        b append ' '
      }
      b.toString() must be equalTo "A11 A12 A13 A31 A32 A33 "
    }
  }

  "functions" should {

    /*
        SftI: 2.7 Functions
     */

    "be specifiable without return type" in {
      def f(x: Int) = x + 1
      f(0) must be equalTo 1
      f(1) must be equalTo 2
      f(2) must be equalTo 3
    }

    "be specified with return type when recursive" in {
      var i = 0
      def f(x: Int): Int = {
        i += 1
        if (x == 0) 0 else f(x - 1)
      }

      f(0) must be equalTo 0
      i must be equalTo 1
      i = 0

      f(1) must be equalTo 0
      i must be equalTo 2
      i = 0

      f(2) must be equalTo 0
      i must be equalTo 3
    }


    /*
        SftI: 2.8 Default and Named Arguments
     */

    "be declared with default parameters" in {
      def decorate(str: String, left: String = "[", right: String = "]") = left + str + right
      decorate("asd", "123", "890") must be equalTo "123asd890"
      decorate("asd", "123") must be equalTo "123asd]"
      decorate("asd", right = "890") must be equalTo "[asd890"
      decorate("asd") must be equalTo "[asd]"
      decorate("asd", right = "r", left = "l") must be equalTo "lasdr"
    }

    /*
        SftI: 2.9 : Variable Arguments
     */

    "be declared with variable arguments" in {
      def f(a: Int*): Int = a.length
      f(1) must be equalTo 1
      f(1, 2) must be equalTo 2
      f(1, 2, 3) must be equalTo 3
      f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20) must be equalTo 20
    }

    "be declared with variable arguments but called with an existing sequence" in {
      def f(a: Int*): Int = a.length
      val s = Seq(11, 12, 13, 14)
      f(s: _*) must be equalTo 4
    }

    "be declared with variable arguments and with an sequence when recursive" in {
      def f(a: Int*): Int = {
        if (a.length == 0) 0
        else a.head + f(a.tail: _*)
      }

      f(11, 12) must be equalTo 23
      f(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1) must be equalTo 12
    }

    "be called with manual cast to 'object' when calling Java methods" in {
      val s = MessageFormat.format(
        "Formatting {0} and {1}.",
        "first value",
        42.asInstanceOf[AnyRef])
      s must be equalTo "Formatting first value and 42."
    }

  }

  // @TODO: Find out how to test lists/iterables/collections.

  /*
      SftI: 2.11 : Lazy Values
   */

  "lazy values" should {

    "be demonstrated" in {
      var c = 0
      def f(): Int = {
        c += 1
        c
      }
      lazy val v = f()

      // 'v' was not yet evaluated, since its marked 'lazy'. 'c' is still 0.
      c must be equalTo 0
      // Checking 'v' will have the lazy initialization be performed, calling 'f' and incrementing 'c' in the process.
      v must be equalTo 1
      c must be equalTo 1
      // Checking 'v' again should not have the initialization called a second time. Should still be equal to 1.
      v must be equalTo 1
      c must be equalTo 1
    }
  }

  /*
      SftI: 2.12 : Exceptions
   */

  "exceptions" should {

    "work with if/else constructs as a 'Nothing' value" in {
      def f(x: Int): Int = {
        // The first branch has type Int, the second has type Nothing. Therefore, the if/else expression also has type Int.
        if (x >= 0) x
        else throw new IllegalArgumentException("x should not be negative.")
      }

      f(0) must be equalTo 0
      f(1) must be equalTo 1
      f(2) must be equalTo 2

      {
        f(-1)
      } must throwA[IllegalArgumentException]

      {
        f(-2)
      } must throwA[IllegalArgumentException]
    }

    // NB: See chapter 14 of SftI for information on pattern matching syntax.
    def divide(i: Int): Double = {
      if (i >= 0) {
        100 / i
      } else {
        throw new IllegalArgumentException("Will not divide by a negative value.")
      }
    }

    def useDivide(i: Int): String = {
      if (i % 123 == 0 && i > 0) {
        throw new Exception("I really, but REALLY, hate multiples of 123.")
      }
      try {
        divide(i).toString
      } catch {
        case ex: IllegalArgumentException => ex.getMessage
        case _: ArithmeticException => "Division by zero" // use _ when you do now need the variable.
      }
    }

    "be catch by pattern matching (1)" in {
      useDivide(1) must be equalTo "100.0"
    }

    "be catch by pattern matching (2)" in {
      useDivide(2) must be equalTo "50.0"
    }

    "be catch by pattern matching (4)" in {
      useDivide(4) must be equalTo "25.0"
    }

    "be catch by pattern matching (0)" in {
      useDivide(0) must be equalTo "Division by zero"
    }

    "be catch by pattern matching (-1)" in {
      useDivide(-1) must be equalTo "Will not divide by a negative value."
    }

    "be catch by pattern matching (246)" in {
      {
        useDivide(246)
      } must throwA[Exception]
    }

    "be used with a 'finally' block" in {
      def f(): Int = {
        var result = 0
        try {
          result = 42
          throw new Exception("not cool, dude...")
        } catch {
          case e: Throwable => result = -1
        } finally {
          result = 0
        }
        result
      }

      f() must be equalTo 0
    }
  }

  /*
      SftI: Exercises on pages 26-27.
   */

  "Exercises on pages 26-27" should {

    "1. The signum of a number is 1 if the number is positive, –1 if it is negative, and 0 if it is zero. Write a function that computes this value." in {

      def signum(i: Int): Int = {
        if (i > 0) 1
        else if (i < 0) -1
        else 0
      }

      signum(42) must be equalTo 1
      signum(981) must be equalTo 1
      signum(0) must be equalTo 0
      signum(-42) must be equalTo -1
      signum(-1894898914) must be equalTo -1
    }

    "2. What is the value of an empty block expression {}? What is its type?" in {
      val f = {}
      f must be equalTo Unit
    }

    "3. Come up with one situation where the assignment x = y = 1 is valid in Scala. (Hint: Pick a suitable type for x.)" in {
      // Cannot chain assignments. Can just declare initial values to multiple variable by listing them, comma separated.
      val x, y: Int = 42
      x must be equalTo 42
      y must be equalTo 42
    }

    "4. Write a Scala equivalent for the Java loop: for (int i = 10; i >= 0; i--) System.out.println(i);" in {
      val l = new util.ArrayList[Int]
      for (x <- 10.to(0, -1)) {
        l add x
      }

      l.size() must be equalTo 11

      l.get(0) must be equalTo 10
      l.get(1) must be equalTo 9
      l.get(2) must be equalTo 8
      l.get(3) must be equalTo 7
      l.get(4) must be equalTo 6
      l.get(5) must be equalTo 5
      l.get(6) must be equalTo 4
      l.get(7) must be equalTo 3
      l.get(8) must be equalTo 2
      l.get(9) must be equalTo 1
      l.get(10) must be equalTo 0
    }

    "4.1. Alternative answer, usiong 'by' for the step." in {
      val b = for (x <- 10 to 0 by -1) yield x
      b must contain(exactly(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)).inOrder
    }

    "5. Write a procedure countdown(n: Int) that prints the numbers from n to 0." in {

      def countdown(n: Int): String = {
        val b = new StringBuilder
        for (n <- n to(0, -1)) {
          b append n
          b append ' '
        }
        b toString() trim()
      }

      countdown(-1) must be equalTo ""
      countdown(0) must be equalTo "0"
      countdown(1) must be equalTo "1 0"
      countdown(2) must be equalTo "2 1 0"
      countdown(13) must be equalTo "13 12 11 10 9 8 7 6 5 4 3 2 1 0"
    }

    "6. Write a for loop for computing the product of the Unicode codes of all letters in a string. For example, the product of the characters in \"Hello\" is 9415087488L." in {
      def product(s: String): Long = {
        var result: Long = 1L
        for (c: Char <- s) {
          result *= c.toLong
        }
        result
      }
      product("Hello") must be equalTo 9415087488L
    }

    "7. Solve the preceding exercise without writing a loop. (Hint: Look at the StringOps Scaladoc.)" in {
      def product(s: String): Long = {
        //        s product // <--- this is not giving the good result.
        var result: Long = 1L
        s foreach (c => result *= c.toLong)
        result
      }
      product("Hello") must be equalTo 9415087488L
    }

    // 8. Write a function product(s : String) that computes the product, as described in the preceding exercises.
    // Already done in last 2 exercises.

    "9. Make the function of the preceding exercise a recursive function." in {
      def product(s: String): Long = {
        if (s.length == 0) return 0
        val first = s.charAt(0).toLong
        if (s.length == 1) return first
        first * product(s drop 1)
      }
      product("Hello") must be equalTo 9415087488L
    }

    //// I do not understand the problem correctly and end up with an infinite recursive loop.
//    "10. Write a function that computes xn, where n is an integer. Use the following recursive definition:" in {
//      var depth = 0
//      def f(x: Int, n: Int): Int = {
//        depth += 1
//        printf("depth=%d  x=%d  n=%d\n", depth, x, n)
//        try {
//          if (n > 0) {
//            // x^n = y^2 if n is even and positive, where y = x^(n / 2).
//            if (n % 2 == 0) {
//              val fval = f(x, n / 2)
//              math.pow(fval, 2)
//            }
//            // xn = x· x^(n – 1) if n is odd and positive.
//            else x * math.pow(x, n - 1)
//          }
//          // x0 = 1.
//          if (n == 0) {
//            1
//          }
//          // xn = 1 / x–n if n is negative.
//          else {
//            1 / f(x, -n)
//          }
//        } finally {
//          depth -= 1
//        }
//      }
//
//      f(4, 2) must be equalTo 16
//    }
  }
}
