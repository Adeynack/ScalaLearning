package p210TraitsTests

import org.specs2.mutable.Specification

import scala.collection.mutable.ListBuffer
import p210Traits._

object TraitsSpec extends Specification {

  "A date array" should {

    val dates = ListBuffer(
      new Date(2014, 11, 22),
      new Date(2014, 11, 23),
      new Date(2014, 12, 22),
      new Date(2015, 12, 22))

    "has 4 items" in {
      dates must have size 4
      dates.append(new Date(2016, 1, 1))
      dates must have size 5
    }

    "add an element" in {
      dates must have size 4
    }
  }
}
