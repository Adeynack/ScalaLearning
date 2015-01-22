package ScalaForTheImpatient

import org.specs2.mutable.Specification

import scala.collection.{mutable => mu}

/**
 *
 * Scala for the Impatient
 *
 * Chapter 6: Objects
 *
 */
object Chapter06Objects extends Specification {

  //
  // 6.1 Singletons
  //

  "singletons" should {

    "be created with the 'Object' construct" in {

      var constructorWasCalled = 0

      object Accounts {

        private var lastNumber = 0
        constructorWasCalled += 1

        def newUniqueNumber() = {
          lastNumber += 1
          lastNumber
        }
      }

      // constructor is not called until the object is used.
      constructorWasCalled must be equalTo 0

      Accounts.newUniqueNumber() must be equalTo 1
      constructorWasCalled must be equalTo 1
      Accounts.newUniqueNumber() must be equalTo 2
      constructorWasCalled must be equalTo 1
      Accounts.newUniqueNumber() must be equalTo 3
      constructorWasCalled must be equalTo 1
    }
  }

  //
  // 6.2 Companion Object
  //

  "companion object" should {

    "be declared with the same name as its companion class" in {

      object Account {
        // The companion object
        private var lastNumber = 0

        private def newUniqueNumber() = {
          lastNumber += 1
          lastNumber
        }

        def apply(initialBalance: Double = 0.0): Account = {
          // companion object has access to private members of the class.
          new Account(initialBalance)
        }
      }

      class Account {
        val id = Account.newUniqueNumber()
        private var _balance = 0.0

        private def this(initialBalance: Double) = {
          this()
          _balance = initialBalance
        }

        def deposit(amount: Double) {
          _balance += amount
        }

        def balance: Double = _balance
      }

      // Constructor syntax.
      val a1 = new Account

      // Companion object's 'apply' syntax.
      val a2 = Account()
      val a3 = Account(23.98)

      a1 must beAnInstanceOf[Account]
      a2 must beAnInstanceOf[Account]
      a3 must beAnInstanceOf[Account]

      a1.id must be equalTo 1
      a2.id must be equalTo 2
      a3.id must be equalTo 3

      a1.balance must be equalTo 0.0
      a2.balance must be equalTo 0.0
      a3.balance must be equalTo 23.98
    }
  }

  //
  //  6.3 Objects Extending a Class or Trait
  //

  "Objects Extending a Class or Trait" in {

    val log = new mu.ArrayBuffer[String]

    abstract class UndoableAction(val description: String) {
      def undo(): Unit

      def redo(): Unit
    }

    object DoNothingAction extends UndoableAction("Do nothing") {
      override def undo(): Unit = log += "_"

      override def redo(): Unit = log += "_"
    }

    class ConcreteAction(description: String) extends UndoableAction(description) {
      override def undo(): Unit = log += s"Undoing $description"

      override def redo(): Unit = log += s"Redoing $description"
    }

    val actions: Array[UndoableAction] = Array(
      new ConcreteAction("a"),
      new ConcreteAction("b"),
      DoNothingAction,
      new ConcreteAction("c"),
      DoNothingAction)

    actions.foreach(_ undo())
    actions.foreach(_ redo())

    log must contain(exactly(
      "Undoing a",
      "Undoing b",
      "_",
      "Undoing c",
      "_",
      "Redoing a",
      "Redoing b",
      "_",
      "Redoing c",
      "_"
    )).inOrder
  }

  //
  //  6.4 The 'apply' method
  //

  // See example in 6.2. An 'apply' method was declared in object 'Account'.

  //
  //  6.5 Application Objects
  //

  // Not demoable in a specification test.

  // Instead of declaring the main method like this:

  //  object Hello {
  //    def main(args: Array[String]) {
  //      println("Hello world!")
  //    }
  //  }

  // just have an object extending 'App' and right directly the code of the main method inside.

  //  object Hello extends App {
  //    println("Hello world!")
  //  }

  "classes with DelayedInit trait" should {

    val container = mu.ArrayBuffer[String]()

    "be initialized with a body directly in the declaration" in {

      def logToTest(content: String) = container += content

      trait ExampleBase extends DelayedInit {

        logToTest("constructor of ExampleBase")

        override def delayedInit(body: => Unit) {
          logToTest("delayedInit in ExampleBase")
          body
        }
      }

      object ExampleObject extends ExampleBase {
        logToTest("body of ExampleObject")
        println("test")
      }

      container must beEmpty

      val example = ExampleObject

      example must beAnInstanceOf[ExampleBase]
      example must beAnInstanceOf[DelayedInit]

      container must contain(exactly(
        "constructor of ExampleBase",
        "delayedInit in ExampleBase",
        "body of ExampleObject")
      ).inOrder
    }
  }

  //
  //  6.6 Enumerations
  //

  "enumerations" should {

    "be defined using the 'Enumeration' trait" in {

      object Colors extends Enumeration {
        val Red, Yellow, Green = Value
      }

      Colors.Red must beAnInstanceOf[Colors.Value]
      Colors.Yellow must beAnInstanceOf[Colors.Value]
      Colors.Green must beAnInstanceOf[Colors.Value]

      var c = Colors.Red
      c must be equalTo Colors.Red
      c.id must be equalTo 0
      c.toString must be equalTo "Red"

      c = Colors.Yellow
      c must be equalTo Colors.Yellow
      c.id must be equalTo 1
      c.toString must be equalTo "Yellow"

      c = Colors.Green
      c must be equalTo Colors.Green
      c.id must be equalTo 2
      c.toString must be equalTo "Green"
    }

    "be defined using the 'Enumeration' trait, and ID and a value" in {

      object Colors extends Enumeration {
        val Red = Value(0, "Stop")
        // Name "Stop" ID 0
        val Yellow = Value(10)
        // Name "Yellow"
        val Green = Value("Go") // ID 11
      }

      Colors.Red must be equalTo Colors.Red
      Colors.Red.id must be equalTo 0
      Colors.Red.toString must be equalTo "Stop"

      Colors.Yellow must be equalTo Colors.Yellow
      Colors.Yellow.id must be equalTo 10
      Colors.Yellow.toString must be equalTo "Yellow"

      Colors.Green must be equalTo Colors.Green
      Colors.Green.id must be equalTo 11 // one more than the last defined value.
      Colors.Green.toString must be equalTo "Go"
    }

    "be shorter to use with import" in {

      object Colors extends Enumeration {
        type Colors = Value
        val Red, Yellow, Green = Value
      }

      import Colors._

      val c = Red

      c must beAnInstanceOf[Colors.Value] // still is valid (the real type)
      c must beAnInstanceOf[Colors] // works because Colors._ is imported. This then refers to the alias 'Color' inside of the 'Color' object.
    }

    "be accessible from their ID" in {
      object Colors extends Enumeration {
        val Red, Yellow, Green = Value
      }
      Colors(0) must be equalTo Colors.Red
      Colors(1) must be equalTo Colors.Yellow
      Colors(2) must be equalTo Colors.Green
    }

    "be accessible by their name" in {
      object Colors extends Enumeration {
        val Red = Value(0, "Stop")
        val Yellow = Value(10)
        val Green = Value("Go") // ID 11
      }

      Colors.withName("Stop") must be equalTo Colors.Red
      Colors.withName("Yellow") must be equalTo Colors.Yellow
      Colors.withName("Go") must be equalTo Colors.Green
    }
  }


  //
  //  Exercises
  //

  // todo: Exercises of chapter 6.

}
