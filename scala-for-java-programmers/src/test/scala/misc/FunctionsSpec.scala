package misc

import java.text.MessageFormat

import org.specs2.mutable.Specification

object FunctionsSpec extends Specification {

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

  /*
      SftI: 2.11 : Lazy Values
   */

  "lazy values" should {

    "just work" in {
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
}
