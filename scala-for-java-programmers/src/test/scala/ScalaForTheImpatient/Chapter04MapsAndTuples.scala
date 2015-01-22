package ScalaForTheImpatient

import org.specs2.mutable.Specification

import scala.collection._

/**
 *
 * Scala for the Impatient
 *
 * Chapter 4 : Maps and Tuples
 *
 */
object Chapter04MapsAndTuples extends Specification {

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
      alice must beAnInstanceOf[Option[Int]]
      alice.isDefined must beTrue
      alice.get must be equalTo 10
      alice must be equalTo Some(10)

      val xxxxx = scores.get("xxxxx")
      xxxxx must beAnInstanceOf[Option[Int]]
      xxxxx.isDefined must beFalse
      xxxxx must beNone
      def getXxxxx = xxxxx.get
      getXxxxx must throwA[NoSuchElementException]
    }
  }

  //
  //  4.3 Updating Map Values
  //

  "updating map values" should {
    "update or add using the parenthesis" in {
      val scores = mutable.Map[String, Int]()
      scores must have length 0

      scores("Bob") = 10
      scores must have length 1
      scores("Bob") must be equalTo 10

      scores("Fred") = 7
      scores must have length 2
      scores("Fred") must be equalTo 7
    }
  }

  "add multiple associations using the += operator" in {
    val scores = mutable.Map[String, Int]()
    scores must have length 0

    scores +=("Bob" -> 10, "Fred" -> 7)
    scores must have length 2
    scores("Bob") must be equalTo 10
    scores("Fred") must be equalTo 7
  }

  "remove single association using the -= operator" in {
    val scores = mutable.Map("Bob" -> 10, "Alice" -> 0, "Fred" -> 7)
    scores must have length 3
    scores must have key "Bob"
    scores must have key "Alice"
    scores must have key "Fred"

    scores -= "Alice"
    scores must have length 2
    scores must have key "Bob"
    scores must not haveKey "Alice"
    scores must have key "Fred"
  }

  "adding an element to an immutable map returns a new map with the modification" in {
    val scores = immutable.Map("Alice" -> 0)
    scores must have length 1

    val newScores = scores +("Bob" -> 10, "Fred" -> 7)
    scores must have length 1
    newScores must beAnInstanceOf[immutable.Map[String, Int]]
    newScores must have length 3
    newScores("Alice") must be equalTo 0
    newScores("Bob") must be equalTo 10
    newScores("Fred") must be equalTo 7

    scores must not beTheSameAs newScores
  }

  "the same using the same identifier and the += operator" in {
    var scores = immutable.Map("Alice" -> 0)
    scores must have length 1

    scores = scores + ("Bob" -> 10)
    scores must have length 2
    scores("Alice") must be equalTo 0
    scores("Bob") must be equalTo 10

    scores += ("Fred" -> 7)
    scores must have length 3
    scores("Alice") must be equalTo 0
    scores("Fred") must be equalTo 7
    scores("Bob") must be equalTo 10
  }

  "removing an element to an immutable map does the same" in {
    val scores = immutable.Map("Alice" -> 0, "Bob" -> 10, "Fred" -> 7)
    val newScores = scores - "Bob"
    scores must not beTheSameAs newScores

    scores must have length 3
    scores("Alice") must be equalTo 0
    scores("Bob") must be equalTo 10
    scores("Fred") must be equalTo 7

    newScores must have length 2
    newScores("Alice") must be equalTo 0
    newScores must not haveKey "Bob"
    newScores("Fred") must be equalTo 7
  }

  //
  //  4.4 Iterating over Maps
  //

  "iterating over Maps" should {

    "with for-tuple syntax" in {
      val scores = immutable.Map("Bob" -> 10, "Fred" -> 7, "Alice" -> 0)
      val sb = new StringBuffer
      for ((k, v) <- scores) sb append s"$k is $v years old. "
      sb.toString must be equalTo "Bob is 10 years old. Fred is 7 years old. Alice is 0 years old. "
    }

    "obtain only keys" in {
      val scores = immutable.Map("Bob" -> 10, "Fred" -> 7, "Alice" -> 0)
      scores.keySet must contain(exactly("Bob", "Fred", "Alice"))
    }

    "obtain only values" in {
      val scores = immutable.Map("Bob" -> 10, "Fred" -> 7, "Alice" -> 0)
      scores.values must contain(exactly(10, 7, 0))
    }

    "be used to simply reverse a map (keys become values and vice versa)" in {
      val scores = immutable.Map("Bob" -> 10, "Fred" -> 7, "Alice" -> 0)
      val reversed = for ((k, v) <- scores) yield (v, k)
      reversed must beAnInstanceOf[immutable.Map[Int, String]]
      reversed must have length 3
      reversed(10) must be equalTo "Bob"
      reversed(7) must be equalTo "Fred"
      reversed(0) must be equalTo "Alice"
    }

    "reverse a map with duplicate values" in {
      // WARNING: Reversing from a map with duplicate values will only retain the last tuple.
      val scores = immutable.Map("Bob" -> 10, "Fred" -> 7, "Alice" -> 10)
      val reversed = for ((k, v) <- scores) yield (v, k)
      reversed must beAnInstanceOf[immutable.Map[Int, String]]
      // ("Bob", 10) was overwriten by ("Alice", 10).
      // ("Fred", 7) stayed.
      reversed must have length 2
      reversed(10) must be equalTo "Alice"
      reversed(7) must be equalTo "Fred"
    }
  }

  //
  //  4.5 Sorted Maps
  //

  "sorted maps" should {

    "be constructed specifying SortedMap (which is a tree map)" in {
      val scores = immutable.SortedMap("Alice" -> 10, "Fred" -> 7, "Bob" -> 3, "Cindy" -> 8)
      scores.keySet must contain(exactly("Alice", "Bob", "Cindy", "Fred")).inOrder
    }

    "for mutable tree map, use Java's TreeMap" in {
      val scores = new java.util.TreeMap[String, Int]
      scores put("Alice", 10)
      scores put("Fred", 7)
      scores put("Bob", 3)
      scores put("Cindy", 8)
      scores.keySet.toArray must be equalTo Array("Alice", "Bob", "Cindy", "Fred")
    }

    "for mutable ordered hash maps, use LinkedHashMap" in {
      val scores = mutable.LinkedHashMap("Alice" -> 10, "Fred" -> 7, "Bob" -> 3, "Cindy" -> 8)
      scores.keySet must contain(exactly("Alice", "Fred", "Bob", "Cindy")).inOrder
    }
  }

  //
  //  4.6 Interoperating with Java
  //

  "inter operating with Java" should {

    "be done on mutable maps" in {
      import scala.collection.JavaConversions.mapAsScalaMap
      val scoresJava = new java.util.TreeMap[String, Int]
      val scoresScala: mutable.Map[String, Int] = scoresJava

      scoresJava size() must be equalTo 0
      scoresScala must have length 0

      scoresJava.put("Alice", 9)
      scoresJava size() must be equalTo 1
      scoresJava.get("Alice") must be equalTo 9
      scoresScala must have length 1
      scoresScala("Alice") must be equalTo 9

      // adding via method "put"
      scoresScala put("Bob", 11)
      scoresScala must have length 2
      scoresScala("Bob") must be equalTo 11
      scoresJava size() must be equalTo 2
      scoresJava.get("Bob") must be equalTo 11

      // adding via operator +=
      scoresScala += "Fred" -> 7
      scoresScala must have length 3
      scoresScala("Fred") must be equalTo 7
      scoresJava size() must be equalTo 3
      scoresJava.get("Fred") must be equalTo 7
    }

    "be done on property maps" in {
      import scala.collection.JavaConversions.propertiesAsScalaMap
      val javaProps = System.getProperties
      javaProps must beAnInstanceOf[java.util.Properties]

      val scalaProps: collection.Map[String, String] = javaProps

      scalaProps must have length javaProps.size()

      //      for ((k, v) <- scalaProps) {
      //        s"both must have the same value for key $k" in {
      //          javaProps.get(k) must be equalTo v
      //        }
      //      }

      //      scalaProps.keySet foreach { k =>
      //        scalaProps(k) must be equalTo javaProps.get(k).toString()
      //      }

      // todo: Tried to do test in loops as described in http://stackoverflow.com/questions/6805267/scalatest-or-specs2-with-multiple-test-cases
      //       but it didn't work.
    }

    // NB: Commented example (from the book) to prevent importing AWT.
    //    "pass a Scala map to a Java method (book example)" in {
    //      import scala.collection.JavaConversions.mapAsJavaMap
    //      import java.awt.font.TextAttribute._
    //      // import the different values of the enum
    //      val attrs = Map(FAMILY -> "Serif", SIZE -> 12) // a Scala map
    //      val font = new java.awt.Font(attrs) // expects a Java map
    //      font.getFamily must be equalTo "Serif"
    //      font.getSize must be equalTo 12
    //    }

    "pass a Scala map to a Java method (home made example)" in {
      import scala.collection.JavaConversions.mapAsJavaMap
      val attributes = Map("One" -> 1, "Two" -> 2, "Three" -> 3)
      val javaObject = new Chapter04MapsAndTuplesSupport

      javaObject.log.toString must be equalTo ""

      javaObject.needAJavaMap(attributes)
      javaObject.log.toString must be equalTo "(One:1),(Two:2),(Three:3)"
    }
  }

  //
  // 4.7 Tuples
  //

  "tuples" should {

    "are created using the parenthesis syntax and elements accessible with _1 (to _n)" in {
      val t = (1, 3.14, "Fred")
      // checking type with full type definition.
      t must beAnInstanceOf[Tuple3[Int, Float, String]]
      // checking the same type with syntaxic sugar (parenthesis).
      t must beAnInstanceOf[(Int, Float, String)]
      t._1 must beAnInstanceOf[Int] and be equalTo 1
      t._2 must beAnInstanceOf[Float] and be equalTo 3.14
      t._3 must beAnInstanceOf[String] and be equalTo "Fred"
    }

    "elements can be accessed by pattern matching to save then in individual variables" in {
      val t = (1, 3.14, "Fred")
      val (x1, x2, x3) = t
      x1 must beAnInstanceOf[Int] and be equalTo t._1 and be equalTo 1
      x2 must beAnInstanceOf[Float] and be equalTo t._2 and be equalTo 3.14
      x3 must beAnInstanceOf[String] and be equalTo t._3 and be equalTo "Fred"

      // For partial access, use _ for the values that are not needed.
      val (y1, _, y2) = t
      y1 must beAnInstanceOf[Int] and be equalTo t._1 and be equalTo 1
      y2 must beAnInstanceOf[String] and be equalTo t._3 and be equalTo "Fred"
    }

    "tuples are useful for functions that return more than one value" in {
      val p = "New York".partition(_.isUpper) // Yields the pair ("NY", "ew ork")
      p must beAnInstanceOf[(String, String)]
      p._1 must be equalTo "NY"
      p._2 must be equalTo "ew ork"
    }
  }

  //
  //  4.8 Zipping
  //

  "zipping" should {

    "join different values together" in {
      val symbols = Array("<", "-", ">")
      val counts = Array(2, 10, 3)

      val pairs = symbols zip counts // symbols.zip(count)

      pairs must beAnInstanceOf[Array[(String, Int)]]
      pairs(0) must beEqualTo("<", 2)
      pairs(1) must beEqualTo("-", 10)
      pairs(2) must beEqualTo(">", 3)

      val pairsString = (for ((s, c) <- pairs) yield s"$s has $c instances.") mkString " "
      pairsString must be equalTo "< has 2 instances. - has 10 instances. > has 3 instances."
    }

    "serve to create maps from different sources" in {
      val symbols = Array("<", "-", ">")
      val counts = Array(2, 10, 3)

      val m: Map[String, Int] = symbols zip counts toMap

      m must beAnInstanceOf[immutable.Map[String, Int]]
      m must have length 3
      m("<") must be equalTo 2
      m("-") must be equalTo 10
      m(">") must be equalTo 3
    }
  }

  //
  //  Exercises
  //

  // todo: Exercises of chapter 4.
}
