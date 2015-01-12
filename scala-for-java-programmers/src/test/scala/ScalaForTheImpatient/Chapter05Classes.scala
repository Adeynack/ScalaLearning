package ScalaForTheImpatient

import java.util.GregorianCalendar

import org.specs2.mutable.Specification

import scala.collection.mutable

object Chapter05Classes extends Specification {

  //
  //  5.1 Simple Classes and Parameterless Methods
  //

  "Simple Classes and Parameterless Methods" should {

    "In its simplest form, a Scala class looks very much like its equivalent in Java" in {
      class Counter {
        private var value = 0

        // you MUST initialize the field.
        def increment(): Unit = value += 1

        def current = value
      }

      // NB: Classes are public.

      // NB: It is common practice to put parenthesis to mutator methods and none for the accessor methods.
      //     increment() <--- mutator, will change the object.
      //     current     <--- accessor, give information about the object without changing it.

      val c = new Counter // or = new Counter(), the same.
      c.current must be equalTo 0
      c increment()
      c.current must be equalTo 1
      c increment()
      c.current must be equalTo 2
    }
  }

  //
  //  5.2 Properties with Getters and Setter
  //

  "Properties with Getters and Setter" should {

    "have automatically getters and setters when a field is public" in {
      class Person {
        var age = 0
      }

      val p = new Person
      p.age must be equalTo 0 // call to automatically created method age() : Int
      p.age = 30 // call to automatically created method age_=(value: Int) : Unit
      p.age must be equalTo 30
    }

    "getter and setters can be manually defined" in {
      class Person {
        private var privateAge = 0
        // make private and rename
        val ageHistory = mutable.ArrayBuffer(privateAge)

        def age = privateAge

        def age_=(newValue: Int): Unit = {
          if (newValue == privateAge) return
          ageHistory += newValue
          privateAge = newValue
        }
      }

      val p = new Person
      p.age must be equalTo 0
      p.ageHistory must contain(exactly(0)).inOrder

      p.age = 1
      p.age must be equalTo 1
      p.ageHistory must contain(exactly(0, 1)).inOrder

      p.age = 2
      p.age must be equalTo 2
      p.ageHistory must contain(exactly(0, 1, 2)).inOrder

      p.age = 1
      p.age must be equalTo 1
      p.ageHistory must contain(exactly(0, 1, 2, 1)).inOrder

      p.age = 1
      p.age must be equalTo 1
      p.ageHistory must contain(exactly(0, 1, 2, 1)).inOrder
    }
  }
  //
  //  5.3 Properties with Only Getters
  //

  "Properties with Only Getters" should {

    "val fields will have only a getter generated" in {
      class Message {
        val timeStamp = new GregorianCalendar(2015, 1, 12) // value given for the purpose of the test, logically we would want 'now' to be the value here.
      }

      import java.util.Calendar._

      val m = new Message
      val ts = m.timeStamp

      ts get YEAR must be equalTo 2015
      ts get MONTH must be equalTo 1
      ts get DAY_OF_MONTH must be equalTo 12

      // Next line would cause a "Reassignment to val".
      // m.timeStamp = new GregorianCalendar(2011, 1, 1)
    }

    "better version of the 'Counter' class of 5.1" in {
      class Counter {
        private var value = 0
        def increment() : Unit = value += 1
        def current = value // No () in declaration.
      }

      val c = new Counter // or = new Counter(), the same.
      c.current must be equalTo 0
      c increment()
      c.current must be equalTo 1
      c increment()
      c.current must be equalTo 2
    }

    //
    //  To summarize, you have four choices for implementing properties:
    //  1. var foo: Scala synthesizes a getter and a setter.
    //  2. val foo: Scala synthesizes a getter.
    //  3. You define methods foo and foo_=.
    //  4. You define a method foo.
    //
  }

  //
  //  5.4 Object-Private Fields
  //

  "object-private fields" should {

    "fields from another instance of a class are accessible (just like in Java of C++)" in {
      class Counter {
        private var value = 0
        def increment(i: Int = 1) : Unit = value += i
        def current = value
        def from(c: Counter) : Unit = value = c.value // 'value' from the other instance is accessible in this instance.
      }

      val a = new Counter
      a.increment(4)
      a.current must be equalTo 4

      val b = new Counter
      b.increment(6)
      b.current must be equalTo 6

      b.from(a)
      b.current must be equalTo 4 // now equal to the current value of 'a'.
      a.current must be equalTo 4 // this didn't change.
    }

  }
}
