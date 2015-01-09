/*
 *
 *    Scala for the Impatient
 *
 *    Chapter 4 : Maps and Tuples
 *
 */
package ScalaForTheImpatient

import org.specs2.mutable.Specification
import scala.collection._

object Chapter04 extends Specification {

  //
  //  4.1 Constructing a Map
  //

  "constructing a map" should {

    "be built immutable from the Map syntax" in {
      val scores = Map("Alice" -> 10, "Bob" -> 3, "Cindy" -> 8)
      scores must beAnInstanceOf[scala.collection.immutable.Map[String, Int]]
      // apparently, collection.Map is a short for collection.immutable.Map
      scores must beAnInstanceOf[scala.collection.Map[String, Int]]
    }

    "be built mutable using scala.collection.mutable.Map" in {
      val scores = mutable.Map("Alice" -> 10.2, "Bob" -> 3.5, "Cindy" -> 8.0)
      scores must beAnInstanceOf[scala.collection.mutable.Map[String, Float]]
      // the real implementation that construct builds is 'mutable.HashMap'.
      scores must beAnInstanceOf[scala.collection.mutable.HashMap[String, Int]]
    }

    "be built empty using the direct type HashMap class" in {
      val scores = new mutable.HashMap[String, Int]
      // it's then an instance of the trait Map
      scores must beAnInstanceOf[scala.collection.mutable.Map[String, Int]]
      // and of class HashMap
      scores must beAnInstanceOf[scala.collection.mutable.HashMap[String, Int]]
    }

    "are built using pairs (although you just don't want to that)" in {
      val scores = Map(("Alice", 10), ("Bob", 3), ("Cindy", 8))
      scores must beAnInstanceOf[scala.collection.immutable.Map[String, Int]]
      scores must beAnInstanceOf[scala.collection.Map[String, Int]]
    }
  }

  //
  //  4.2 Accessing Map Values
  //

  "accessing map values" should {

    "done with the parenthesis syntax" in {
      val scores = Map(("Alice", 10), ("Bob", 3), ("Cindy", 8))
      scores("Alice") must be equalTo 10
      scores("Bob") must be equalTo 3
      scores("Cindy") must be equalTo 8
    }

    "throw an exception then the key is not present" in {
      val scores = Map(("Alice", 10), ("Bob", 3), ("Cindy", 8))
      def getAlice = scores("I do not exist")
      getAlice must throwA[NoSuchElementException]
    }

    "pre-checked using 'contains'" in {
      val scores = Map(("Alice", 10), ("Bob", 3), ("Cindy", 8))
      (if (scores.contains("Alice")) scores("Alice") else -1) must be equalTo 10
      (if (scores.contains("xxxxx")) scores("xxxxx") else -1) must be equalTo -1
    }

    "quickly safe-called using 'getOrElse'" in {
      val scores = Map(("Alice", 10), ("Bob", 3), ("Cindy", 8))
      scores.getOrElse("Alice", -1) must be equalTo 10
      scores.getOrElse("xxxxx", -1) must be equalTo -1
    }

    "be fetched using an Option return type with the 'get' method" in {
      val scores = Map(("Alice", 10), ("Bob", 3), ("Cindy", 8))

      val alice = scores.get("Alice")
      alice.isDefined must beTrue
      alice.get must be equalTo 10
      alice must be equalTo Some(10)

      val xxxxx = scores.get("xxxxx")
      xxxxx.isDefined must beFalse
      xxxxx must beNone
      def getXxxxx = xxxxx.get
      getXxxxx must throwA[NoSuchElementException]
    }
  }

  //
  //  4.3 Updating Map Values
  //

//  "updating map values" should {
//
//  }
}
