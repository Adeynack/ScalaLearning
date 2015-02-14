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

  }

}
