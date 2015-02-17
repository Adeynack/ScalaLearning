package misc

import org.specs2.mutable.Specification

class MapSpec extends Specification {

  "list of element" should {

    case class Element(parent: String, children: Int)

    val elements = List(
      Element("A", 101),
      Element("A", 102),
      Element("A", 103),
      Element("B", 201),
      Element("B", 202),
      Element("C", 301),
      Element("C", 302),
      Element("C", 303),
      Element("C", 304))

    "be groupable" in {
      val m: Map[String, List[Element]] = elements.groupBy(_.parent)

      m must beAnInstanceOf[Map[String, Element]]
      m.contains("A") must beTrue
      m("A") must have length 3
      m.contains("B") must beTrue
      m("B") must have length 2
      m.contains("C") must beTrue
      m("C") must have length 4
    }

    "be groupable with sub-element as the value" in {
      val m: Map[String, List[Int]] = elements.groupBy(_.parent).mapValues(_.map(_.children))

      m must beAnInstanceOf[Map[String, Int]]
//      m.contains("A") must beTrue
//      m("A") must have length 3
//      m.contains("B") must beTrue
//      m("B") must have length 2
//      m.contains("C") must beTrue
//      m("C") must have length 4

    }
  }

}
