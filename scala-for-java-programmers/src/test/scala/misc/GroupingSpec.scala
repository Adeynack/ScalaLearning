package misc

import com.github.nscala_time.time.Imports._
import org.specs2.mutable.Specification

class GroupingSpec extends Specification {
  sequential

  case class Item(id: Int,
                  name: String,
                  path: String,
                  updatedOn: DateTime,
                  externalId: Int)

  val v1 = Item(1, "Aaaa", "/aaaa-page/", new DateTime(2015, 2, 19, 9, 52), 4827)
  val v2 = Item(2, "Bbbb", "/bbbb-page-uk/", new DateTime(2015, 1, 1, 0, 0), 9532)
  val v3 = Item(3, "Bbbb", "/bbbb-page-de/", new DateTime(2015, 2, 10, 12, 1), 4981)
  val v4 = Item(4, "Aaaa", "/aaaa-page/", new DateTime(2015, 2, 22, 21, 56), 1235)
  val v5 = Item(5, "Aaaa", "/aaaa-page/", new DateTime(2015, 2, 22, 23, 24), 6532)
  val v6 = Item(6, "Cccc", "/cccc-page/", new DateTime(2015, 2, 25, 1, 14), 235)
  val items = Seq(v1, v2, v3, v4, v5, v6)

  "grouping by single field" should {
    sequential

    "be possible with 'groupBy'" in {
      val grouped = items.groupBy(_.name)
      grouped must beAnInstanceOf[Map[String, Seq[Item]]]
      grouped must have length 3
      grouped must haveKeys("Aaaa", "Bbbb", "Cccc")
      val a = grouped("Aaaa")
      a must contain(exactly(v1, v4, v5))
      val b = grouped("Bbbb")
      b must contain(exactly(v2, v3))
      val c = grouped("Cccc")
      c must contain(exactly(v6))
    }

    "be possible with mapped value specification (sorted by inverted update)" in {
      // grouped by name, and then sorted by descending dates.
      def sortByDescUpdate(s: Seq[Item]) = s.sortBy(_.updatedOn).reverse
      val grouped = items.groupBy(_.name).mapValues(sortByDescUpdate)
      grouped must beAnInstanceOf[Map[String, Seq[Item]]]
      grouped must have length 3
      grouped must haveKeys("Aaaa", "Bbbb", "Cccc")
      val a = grouped("Aaaa")
      a must contain(exactly(v5, v4, v1)).inOrder
      val b = grouped("Bbbb")
      b must contain(exactly(v3, v2)).inOrder
      val c = grouped("Cccc")
      c must contain(exactly(v6)).inOrder
    }

    "be possible with mapped value specification (sorted by extracted externalId)" in {
      // grouped by name, and then sorted by descending dates.
      val grouped = items.groupBy(_.name).mapValues(_.map(_.externalId).sorted)
      grouped must beAnInstanceOf[Map[String, Seq[Int]]] // now a seq of Int
      grouped must have length 3
      grouped must haveKeys("Aaaa", "Bbbb", "Cccc")
      val a = grouped("Aaaa")
      a must contain(exactly(1235, 4827, 6532)).inOrder
      val b = grouped("Bbbb")
      b must contain(exactly(4981, 9532)).inOrder
      val c = grouped("Cccc")
      c must contain(exactly(235)).inOrder
    }

    "allow the extraction of the last updated item for each name" in {
      // grouped by name, and then sorted by descending dates.
      def getLastUpdated(items: Seq[Item]) = {
        items.sortBy(_.updatedOn).reverse.head
      }
      val grouped = items.groupBy(_.name).mapValues(getLastUpdated)
      grouped must beAnInstanceOf[Map[String, Item]]
      grouped must have length 3
      grouped must haveKeys("Aaaa", "Bbbb", "Cccc")
      val a = grouped("Aaaa")
      a must be equalTo v5
      val b = grouped("Bbbb")
      b must be equalTo v3
      val c = grouped("Cccc")
      c must be equalTo v6
    }

    "be groupable by multiple elements (tuple)" in {
      val grouped = items.groupBy(i => (i.name, i.path)).mapValues(_.sortBy(_.id))
      grouped must beAnInstanceOf[Map[(String, String), Seq[Item]]]
      grouped.keys must contain(exactly(
        ("Aaaa", "/aaaa-page/"),
        ("Bbbb", "/bbbb-page-uk/"),
        ("Bbbb", "/bbbb-page-de/"),
        ("Cccc", "/cccc-page/")
      )).exactly
      val a = grouped(("Aaaa", "/aaaa-page/"))
      a must contain(exactly(v1, v4, v5)).inOrder
      val bUk = grouped(("Bbbb", "/bbbb-page-uk/"))
      bUk must contain(exactly(v2)).inOrder
      val bDe = grouped(("Bbbb", "/bbbb-page-de/"))
      bDe must contain(exactly(v3)).inOrder
      val c = grouped("Cccc", "/cccc-page/")
      c must contain(exactly(v6)).inOrder
    }
  }
}
