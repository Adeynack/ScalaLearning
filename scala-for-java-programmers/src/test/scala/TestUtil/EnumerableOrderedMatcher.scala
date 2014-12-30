//package TestUtil
//
//import org.specs2.matcher.Matcher
//
//case class containsExactly[T](i : java.lang.Iterable[T]) extends Matcher[java.lang.Iterable[T]]() {
//
//  def apply(other: Iterable[T]) = {
//    val iteratorA = i.iterator
//    val iteratorB = other.iterator
//    var failureReason : String = null
//
//    while (failureReason == null && iteratorA.hasNext) {
//      if (iteratorB.hasNext) {
//        if (!iteratorA.next().equals(iteratorB.next())) {
//          failureReason = "Non equal elements at same index."
//        }
//      } else {
//        failureReason = "Left has more elements than right."
//      }
//    }
//
//    if (iteratorB.hasNext) {
//      failureReason = "Left has less elements than right."
//    }
//
//    if (failureReason == null) {
//      (true, "Iterables have the exact same content.", "")
//    } else {
//      (false, "", failureReason)
//    }
//  }
//}
