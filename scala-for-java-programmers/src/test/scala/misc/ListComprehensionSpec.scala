package misc

import org.specs2.mutable.Specification

object ListComprehensionSpec extends Specification {

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

    "work with 'select' logic with multiline syntax" in {
      val b = StringBuilder.newBuilder
      for {i <- 10 to(30, 10)
           j <- 1 to 3
           r = "A%d".format(i + j)} {
        b append r
        b append ' '
      }
      b.toString() must be equalTo "A11 A12 A13 A21 A22 A23 A31 A32 A33 "
    }
  }
}

// TODO: Find out how to test lists/iterables/collections.
