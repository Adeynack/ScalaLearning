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

}
