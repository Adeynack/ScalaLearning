package ScalaForTheImpatient

import org.specs2.mutable.Specification

/**
 *
 * Scala for the Impatient
 *
 * Chapter 8: Inheritance
 *
 */
object Chapter08Inheritance extends Specification {

  //
  // 8.1: Extending a Class
  //

  "extending a class" should {
    "be done with the 'extends' keyword" in {

      class Person {
        var name: String = "" // initial value must be specified, otherwise, the 'val' is abstract.
      }

      class Employee extends Person

      val e = new Employee
      e must beAnInstanceOf[Employee]
      e must beAnInstanceOf[Person]
      e.name must be equalTo ""

      e.name = "David"
      e.name must be equalTo "David"

      val p = e.asInstanceOf[Person]
      p.name must be equalTo "David"
      p must beAnInstanceOf[Person]
      p must beAnInstanceOf[Employee]
    }
  }

  //
  // 8.2 Overriding Methods
  //

  "overriding methods" should {

    "require the 'override' modifier" in {
      class Person {
        val name: String = "Nemo"
      }

      class David extends Person {
        override val name: String = "David"
      }

      val d = new David
      d must beAnInstanceOf[David]
      d must beAnInstanceOf[Person]
      d.name must be equalTo "David"
      d.asInstanceOf[Person].name must be equalTo "David"
    }

    "do not require the 'override' modifier when implementing an abstract method" in {
      abstract class Person {
        val name: String
      }
      class Jurgen extends Person {
        val name: String = "Jürgen"
      }
      val d = new Jurgen
      d.name must be equalTo "Jürgen"
    }
  }

  "accessing the mother class" should {
    "be done with 'super'" in {
      class Person {
        def name: String = "Nemo"
      }
      class Jurgen extends Person {
        // NB: here, override is needed since 'name' has an implementation in the base class.
        // NB: super cannot be used on 'val' type members.
        override def name: String = "Jürgen " + super.name
      }
      val d = new Jurgen
      d.name must be equalTo "Jürgen Nemo"
    }

    //
    // 8.3 Type checks and Casts
    //

    class A {
      def getFromA: String = "A"
    }

    class B extends A {
      def getFromB: String = "B"
    }

    "type check" should {

      "be done using 'isInstanceOf'" in {
        val a = new A
        a.isInstanceOf[A] must beTrue
        a.isInstanceOf[B] must beFalse
        val b = new B
        b.isInstanceOf[A] must beTrue
        b.isInstanceOf[B] must beTrue
      }

      "be false is value is null" in {
        var a = new A
        a.isInstanceOf[A] must beTrue
        a = null
        a.isInstanceOf[A] must beFalse
      }
    }

    "type cast" should {

      "be done using 'asInstanceOf'" in {
        val b = new B
        val a = b.asInstanceOf[A]
        a.isInstanceOf[A] must beTrue
      }

      "fail with an exception if the cast is impossible" in {
        def castAtoB: B = {
          val a = new A
          a.asInstanceOf[B]
        }
        castAtoB must throwA[ClassCastException]
      }

      "be done for specific class (not subclasses) using 'getClass' and 'classOf'" in {
        val a = new A
        (a.getClass == classOf[A]) must beTrue
        (a.getClass == classOf[B]) must beFalse
        val b = new B
        (b.getClass == classOf[A]) must beFalse
        (b.getClass == classOf[B]) must beTrue
      }

      "be done better using pattern matching" in {
        def getValue(v: A): String =
          v match {
            case b: B => b.getFromB // use 'a' like a 'b'
            case _ => v.getFromA // for all other cases
          }

        val aAsA: A = new A
        getValue(aAsA) must be equalTo "A"

        val bAsA: A = new B
        getValue(bAsA) must be equalTo "B"
      }
    }

    //
    // 8.4 Protected fields and Methods
    //

    "protected member" should {

      "be declared using the 'protected' modifier" in {

        class A {
          protected def getProtectedValue = "I am protected"
        }

        class B extends A {
          def getValue = super.getProtectedValue
        }

        // (new A).getProtectedValue --> causes the following compile error:
        // Access to protected method getProtectedValue not permitted [...]

        val b = new B
        b.getValue must be equalTo "I am protected" // ... but accessible through child classes!
      }

      "be limited to the current instance using [this]" in {

        class A(value: String) {
          protected val protectedButAccessibleFromOtherInstances = value
          protected[this] val protectedToThisInstance = "accessible from this instance only"
          def this(other: A) {
            // this(other.protectedToThisInstance) <--- illegal statement because protected to [this] only.
            this(other.protectedButAccessibleFromOtherInstances) // legal statement.
          }
        }

        class B(other: A) extends A(other) {
          def getProtectedValueFromMotherClass = protectedButAccessibleFromOtherInstances
        }

        val a = new A("this is a test")
        val b = new B(a)
        b.getProtectedValueFromMotherClass must be equalTo "this is a test"
      }
    }

    //
    // 8.5 Superclass Construction
    //

    // This is demonstrated a couple of times before. The important thing to mention is that
    // it is only possible to call ONE mother-class constructor (the one used when specifying
    // 'extends MotherClass(constructorParameters)'.
    //
    // The following patter is possible in Java, but not in Scala:
    //
    // Class A                                Class B extends A
    // - ctor(int, int)                  <----- ctor(int, int)
    //     ^
    // - ctor(int) calling ctor(int int) <----- ctor(int)
    //
    // In Scala, it would have to be something like this:
    //
    // Class A                                Class B extends A
    // - ctor(int, int)                  <----- ctor(int, int)
    //     ^                                      ^
    // - ctor(int) calling ctor(int int)        ctor(int)
    //





  }

}
