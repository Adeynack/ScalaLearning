package misc

import org.specs2.mutable.Specification

/**
 * Reference: http://www.vasinov.com/blog/16-months-of-functional-programming/
 */
class TypeVarianceSpec extends Specification {

  case class InvariantContainer[A](element: A)

  case class CovariantContainer[+A](private val innerElement: A) {
    def get: A = innerElement // <--- does compile. Covariant type A can be used as an output type.
  }

  case class ContravariantContainer[-A]() { // <--- cannot take a A as a kept value: contravariant type A occurs in covariant position in type => A of value innerElement
    def check(other: A): Boolean = true
    // def get: A = innerElement <--- do not compile: contravariant type A occurs in covariant position
  }

  class Person {
    def somePersonStuff: String = "Person stuff"
  }

  class User extends Person {
    def someUserStuff: String = "User stuff"
  }

  class Admin extends User {
    def someAdminStuff: String = "Admin stuff"
  }

  "calling contravariant stuff not in the implementation" should {
    "is accepted as an input parameter, but cannot be used as a return type." in {
      val con3: ContravariantContainer[User] = ContravariantContainer[Person]() // works
      val other = new Admin
      con3.check(other) must beTrue
    }
  }

}
