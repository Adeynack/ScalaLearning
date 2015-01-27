package ScalaForTheImpatient

import org.specs2.mutable.Specification

/**
*
* Scala for the Impatient
*
* Chapter 7: Packages and Imports
*
*/
object Chapter07PackagesAndImports extends Specification {

  //
  //  7.1 Packages
  //
  "packages" should {
    "be declared in hierarchy" in {
      val a = foo.bar.Ding(1, "Asdf")
      a.id must be equalTo 1
      a.name must be equalTo "Asdf"
    }
  }

  //
  //  7.2 Scope Rules
  //

  // See Chapter07_02. Elements from parent packages are accessible.

  //
  //  7.3
  //

  //
  //  7.4 Top-of-file Notation
  //
  "top-of-file notation" should {
    "define packages" in {
      val a = Tata.Titi.Tutu.Toto.Machin
      a must beAnInstanceOf[_root_.Tata.Titi.Tutu.Toto.Machin.type]

      val b = new Tata.Titi.Tutu.Toto.Truc()
      b must beAnInstanceOf[_root_.Tata.Titi.Tutu.Toto.Truc]
    }
  }

  //
  //  7.5 Package Object
  //
  "package object" should {
    "be accessible" in {
      import ScalaForTheImpatient.ThisIsAnExample._
      val a = new Something
      a.a must be equalTo FirstConstant // from the package object
      a.b must be equalTo SecondConstant
      a.c must be equalTo ThirdConstant

      val valueFromF = Etwas.f
      valueFromF must be equalTo FirstConstant + SecondConstant + ThirdConstant
    }
  }

  //
  //  7.6 Package Visibility
  //

  object Person {
    private[ScalaForTheImpatient] val thisIsVisibleFromAllThePackage = 42
  }

  "qualified members with package name" should {
    "be visible within all the package" in {
      Person.thisIsVisibleFromAllThePackage must be equalTo 42
    }
  }

  //
  // This will be used in the next examples.
  //
  object Colors extends Enumeration {
    val Red, Yellow, Green = Value
  }

  //
  //  7.7 Imports
  //

  "imports" should {

    "import direct classes" in {
      import java.util.ArrayDeque
      val l = new ArrayDeque[Int]()
      l must beAnInstanceOf[java.util.ArrayDeque[Int]]
    }

    "import all package" in {
      import java.util._
      val l = new ArrayDeque[Int]()
      l must beAnInstanceOf[ArrayDeque[Int]]
      l must beAnInstanceOf[Deque[Int]]
    }

    "import all members of a class" in {
      import java.util.Map._
      // 'Entry' is a type inside of interface 'Map'.
      val e: Entry[Int, String] = new Entry[Int, String] {
        override def getKey: Int = 42

        override def getValue: String = "Zweiundvierzehn"

        override def setValue(value: String): String = value
      }
      e must beAnInstanceOf[Entry[Int, String]]
    }

    "import all values of an enum (or object)" in {

      import Colors._

      // Accessing values of 'Colors' without prefixing with the object/enum's name.
      Red must be equalTo Colors.Red
      Yellow must be equalTo Colors.Yellow
      Green must be equalTo Colors.Green
    }
  }

  //
  //  7.8 Imports can be everywhere
  //
  //  It's already demonstrated through all the examples here.
  //

  //
  //  7.9 Renaming and Hiding Members
  //

  "import selectors" should {

    "import few members" in {
      import Colors.{Red, Green}
      Red must be equalTo Colors.Red
      // Yellow must be equalTo Colors.Yellow <---- not possible. 'Yellow'
      Colors.Yellow must be equalTo Colors.Yellow // <--- must be prefixed with object/enum name.
      Green must be equalTo Colors.Green
    }

    "rename members" in {
      import scala.collection.mutable.{Map => MuMap}
      val m = MuMap(1 -> "Eins", 2 -> "Zwei", 3 -> "Drei")
      m must beAnInstanceOf[scala.collection.mutable.Map[Int, String]]
    }

    "hide members" in {
      import java.util.{HashMap => _, _} // hides HashMap but imports the rest of the package.
      import scala.collection.mutable._

      val hm = HashMap(1 -> 10, 2 -> 20, 3 -> 30)
      hm must beAnInstanceOf[scala.collection.mutable.HashMap[Int, Int]]

      val al = new ArrayList[Int]()
      al must beAnInstanceOf[java.util.ArrayList[Int]]
    }
  }

  //
  //  7.10 Implicit Imports
  //

  "implicit imports" should {

    "allow shorter import specifications" in {
      import collection.mutable._ // not mandatory to write scala. in the beginning.
      val b = Buffer(1, 2, 3)
      b must beAnInstanceOf[scala.collection.mutable.Buffer[Int]]
    }

    "imports 'prefef'" in {
      val f = println
      f must be equalTo scala.Predef.println
    }
  }

  //
  //  Exercises
  //

  // todo: Exercises of chapter 7.

}
