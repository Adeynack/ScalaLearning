package ScalaForTheImpatient

import java.util.GregorianCalendar

import org.specs2.mutable.Specification

import scala.beans.BeanProperty
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

        def increment(): Unit = value += 1

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

        def increment(i: Int = 1): Unit = value += i

        def current = value

        def from(c: Counter): Unit = value = c.value // 'value' from the other instance is accessible in this instance.
      }

      val a = new Counter
      a increment 4
      a.current must be equalTo 4

      val b = new Counter
      b increment 6
      b.current must be equalTo 6

      b from a
      b.current must be equalTo 4 // now equal to the current value of 'a'.
      a.current must be equalTo 4 // this didn't change.
    }

    "fields can be private to the specific instance they are owned by" in {
      class Counter {

        // NB: this field can only be accessed from THIS instance.
        private[this] var value = 0

        def increment(i: Int = 1): Unit = value += i

        def current = value

        def from(c: Counter): Unit = value = c.current // NB: Must use something public to access the value, now that it is instance-private.
      }

      val a = new Counter
      a increment 4
      a.current must be equalTo 4

      val b = new Counter
      b increment 6
      b.current must be equalTo 6

      b from a
      b.current must be equalTo 4 // now equal to the current value of 'a'.
      a.current must be equalTo 4 // this didn't change.
    }
  }

  //
  //  5.5 Bean Properties
  //

  "bean properties" should {

    "be defined using the annotation around fields" in {
      class Person(private val initIdentifier: Int, private val initNickname: String) {

        // generates public 'name' and 'name_=' (Scala getter and setter)
        var firstName: String = ""

        // generates public 'lastName', 'lastName_=' (Scala getter and setter) + 'getLastName' and 'setLastName' (Java getter and setter)
        @BeanProperty var lastName: String = ""

        // generates public 'fullName' only (Scala getter)
        val nickname: String = initNickname

        // generates public 'identifier' (Scala getter) and 'getIdentifier' (Java getter)
        @BeanProperty val identifier: Int = initIdentifier

        // generates private 'foo' (Scala getter)
        private var foo: Int = 42

        // generates instance only getter 'foo'. Will still generate some code that would allow Java to access it from another instance,
        // since the JVM itself does not allow that grain of accessibility.
        private[this] var bar: String = "kapoue"

        def setFooBar(value: (Int, String)): Unit = {
          foo = value._1
          bar = value._2
        }

        //      def getFooBar: (Int, String) = (foo, bar)
        def getFooBar: String = s"$foo $bar"

        // if, per instance, Person would be an nested class inside a PersonFactory class, it would be possible to allow
        // the enclosing class to access the field by specifying the type instead of this in the []. Example:
        // private[PersonFactory] something: String = ""
      }

      val p = new Person(1, "OncleGeorge")
      p.firstName must be equalTo ""
      p.lastName must be equalTo ""
      p.getLastName must be equalTo "" // Java Bean 'get' call.
      p.nickname must be equalTo "OncleGeorge"
      p.identifier must be equalTo 1
      p.getIdentifier must be equalTo 1 // Java Bean 'get' call.
      p.getFooBar must be equalTo "42 kapoue"

      p.firstName = "Oncle"
      p.lastName = "George"
      p setFooBar(53, "bouhou")
      p.firstName must be equalTo "Oncle"
      p.lastName must be equalTo "George"
      p.getLastName must be equalTo "George" // Java Bean 'get' call.
      p.nickname must be equalTo "OncleGeorge"
      p.identifier must be equalTo 1
      p.getIdentifier must be equalTo 1 // Java Bean 'get' call.
      p.getFooBar must be equalTo "53 bouhou"
    }
  }

  //
  //  5.6 Auxiliary Constructors
  //  5.7 Primary Constructors
  //

  "constructors" should {

    "be declared" in {

      var instanceCounter = 0

      class Person(val name: String, val age: Int) {
        // content of the default constructor.
        instanceCounter += 1

        // properties (as part of the constructor. Doesn't change anything in the interface of the class if defined
        // before, during or after the constructor's content.
        val usedConstructor = new mutable.ArrayBuffer[String]

        // rest of the content of the default constructor
        // NB: Since 'usedConstructor' is used in the constructor, it has to be defined before this part of the constructor.
        usedConstructor += "default"

        // methods definition
        def this(name: String) = {
          this(name, 0) // calling the default constructor.
          usedConstructor += "auxiliary with name"
        }

        def this() = {
          this("Nemo") // calling the auxiliary constructor with name only.
          usedConstructor += "auxiliary with no parameter"
        }
      }

      instanceCounter must be equalTo 0

      val david = new Person("David", 33)
      david.name must be equalTo "David"
      david.age must be equalTo 33
      david.usedConstructor must contain(exactly("default")).inOrder
      instanceCounter must be equalTo 1

      val bob = new Person("Bob")
      bob.name must be equalTo "Bob"
      bob.age must be equalTo 0
      bob.usedConstructor must contain(exactly("default", "auxiliary with name")).inOrder
      instanceCounter must be equalTo 2

      val nemo = new Person
      nemo.name must be equalTo "Nemo"
      nemo.age must be equalTo 0
      nemo.usedConstructor must contain(exactly("default", "auxiliary with name", "auxiliary with no parameter")).inOrder
      instanceCounter must be equalTo 3
    }

    "multiple constructors are often avoidable by using primary constructor parameter values" in {

      var instanceCounter = 0

      class Person(val name: String = "Nemo", val age: Int = 0) {
        instanceCounter += 1
      }

      instanceCounter must be equalTo 0

      val david = new Person("David", 33)
      david.name must be equalTo "David"
      david.age must be equalTo 33
      instanceCounter must be equalTo 1

      val bob = new Person("Bob")
      bob.name must be equalTo "Bob"
      bob.age must be equalTo 0
      instanceCounter must be equalTo 2

      val nemo = new Person
      nemo.name must be equalTo "Nemo"
      nemo.age must be equalTo 0
      instanceCounter must be equalTo 3
    }

    "properties defined as constructor parameters can have the same declarations than those in 5.1" in {
      class Person(@BeanProperty val identifier: Int,
                   val nickname: String,
                   var firstName: String = "",
                   @BeanProperty var lastName: String = "",
                   private var foo: Int = 42,
                   private[this] var bar: String = "kapoue") {

        def setFooBar(value: (Int, String)): Unit = {
          foo = value._1
          bar = value._2
        }

        def getFooBar: String = s"${foo}is${bar.capitalize}" // NB: ${} is useful when no space follow the name of a variable.
      }

      val p = new Person(1, "OncleGeorge")
      p.firstName must be equalTo ""
      p.lastName must be equalTo ""
      p.getLastName must be equalTo "" // Java Bean 'get' call.
      p.nickname must be equalTo "OncleGeorge"
      p.identifier must be equalTo 1
      p.getIdentifier must be equalTo 1 // Java Bean 'get' call.
      p.getFooBar must be equalTo "42isKapoue"

      p.firstName = "Oncle"
      p.lastName = "George"
      p setFooBar(53, "bouhou")
      p.firstName must be equalTo "Oncle"
      p.lastName must be equalTo "George"
      p.getLastName must be equalTo "George" // Java Bean 'get' call.
      p.nickname must be equalTo "OncleGeorge"
      p.identifier must be equalTo 1
      p.getIdentifier must be equalTo 1 // Java Bean 'get' call.
      p.getFooBar must be equalTo "53isBouhou"
    }

    "parameters without 'val' or 'var' in the primary constructor will be automatically generate a private 'val' when used in a method" in {
      class Test(val foo: Int, val bar: Int, hidden: Int) {
        def getHiddenValue: Int = hidden
      }
      val t1 = new Test(1, 2, 3)
      t1.foo must be equalTo 1
      t1.bar must be equalTo 2
      // t1.hidden must be equalTo 3 // <--- this is not accessible.
      t1.getHiddenValue must be equalTo 3
    }

    "parameters without 'val' or 'var' in the primary constructor will be only be a constructor parameter if not accessed in the class" in {
      class Test(val foo: Int, val bar: Int, hidden: Int) {
        val jkl = hidden
      }
      val t1 = new Test(1, 2, 3)
      t1.foo must be equalTo 1
      t1.bar must be equalTo 2
      // t1.asdf must be equalTo 3 // <--- this is not accessible.
      t1.jkl must be equalTo 3
    }

    "primary constructor can be private, forcing the use of an auxiliary constructor" in {
      var instanceCounter = 0

      class Person private(val name: String, val age: Int) {
        // content of the primary constructor.
        instanceCounter += 1

        // properties (as part of the constructor. Doesn't change anything in the interface of the class if defined
        // before, during or after the constructor's content.
        val usedConstructor = new mutable.ArrayBuffer[String]

        // rest of the content of the primary constructor
        // NB: Since 'usedConstructor' is used in the constructor, it has to be defined before this part of the constructor.
        usedConstructor += "default"

        // methods definition
        def this(name: String) = {
          this(name, 42) // calling the primary constructor.
          usedConstructor += "auxiliary with name"
        }

        def this() = {
          this("Nemo", 0) // calling the primary constructor.
          usedConstructor += "auxiliary with no parameter"
        }
      }

      object Person {
        // NB: It's still possible to call that constructor from a 'companion object'.
        def apply(name: String, age: Int): Person = new Person(name, age)
      }

      instanceCounter must be equalTo 0

      // val david = new Person("David", 33) // <---- this becomes impossible.
      val david = Person("David", 33) // <----------- constructing using the companion object and its 'apply' method.
      david.name must be equalTo "David"
      david.age must be equalTo 33
      instanceCounter must be equalTo 1

      val bob = new Person("Bob")
      bob.name must be equalTo "Bob"
      bob.age must be equalTo 42
      bob.usedConstructor must contain(exactly("default", "auxiliary with name")).inOrder
      instanceCounter must be equalTo 2

      val nemo = new Person
      nemo.name must be equalTo "Nemo"
      nemo.age must be equalTo 0
      nemo.usedConstructor must contain(exactly("default", "auxiliary with no parameter")).inOrder
      instanceCounter must be equalTo 3
    }
  }

  //
  //  5.8 Nested Classes
  //

  "nested classes" should {

    "nest a class inside another class" in {
      class Network {

        class Member(val name: String) {
          val contacts = new mutable.ArrayBuffer[Member]
        }

        private val members = new mutable.ArrayBuffer[Member]

        def join(name: String): Member = {
          val m = new Member(name)
          members += m
          m
        }
      }

      val chatter = new Network
      val myFace = new Network

      val fred = chatter join "Fred"
      val wilma = chatter join "Wilma"
      fred must beAnInstanceOf[chatter.Member] // the type is linked to the instance.
      wilma must beAnInstanceOf[chatter.Member]

      fred.contacts += wilma // OK
      val barney = myFace.join("Barney") // has type myFace.Member
      barney must beAnInstanceOf[myFace.Member]

      // fred.contacts += barney
      //
      // This previous line cannot compile.
      //  type mismatch;
      //    [error]  found   : myFace.Member
      //    [error]  required: chatter.Member
      //
    }

    "another way of nesting class for most flexibility" in {
      // Move the nested class in a 'companion object'.
      object Network {
        class Member(val name: String) {
          val contacts = new mutable.ArrayBuffer[Member]
        }
      }
      class Network {
        private val members = new mutable.ArrayBuffer[Network.Member]

        def join(name: String): Network.Member = {
          val m = new Network.Member(name)
          members += m
          m
        }
      }

      val chatter = new Network
      val myFace = new Network

      val fred = chatter join "Fred"
      val wilma = chatter join "Wilma"
      fred must beAnInstanceOf[Network.Member]
      wilma must beAnInstanceOf[Network.Member]

      fred.contacts += wilma
      val barney = myFace.join("Barney")
      barney must beAnInstanceOf[Network.Member] // members of 'myFace' are now same type as those of 'chatter'.

      fred.contacts += barney
      //
      // This previous line is now possible, since members of both networks are just 'members' (more general).
      //
      fred.contacts must contain(barney)
    }

    "using member projection (instead of a companioncompanion object) to specify any instance of the parent class" in {
      class Network {

        class Member(val name: String) {
          val contacts = new mutable.ArrayBuffer[Network#Member] // <---- Network#Member means 'a member from any network'.
        }

        private val members = new mutable.ArrayBuffer[Network#Member] // <--- keeping an array of 'member of any network'.

        def join(name: String): Member = {
          val m = new Member(name)
          members += m
          m
        }
      }

      val chatter = new Network
      val myFace = new Network

      val fred = chatter join "Fred"
      val wilma = chatter join "Wilma"
      fred must beAnInstanceOf[chatter.Member] // the type is linked to the instance.
      wilma must beAnInstanceOf[chatter.Member]

      fred.contacts += wilma // OK
      val barney = myFace.join("Barney") // has type myFace.Member
      barney must beAnInstanceOf[myFace.Member]

      fred.contacts += barney
      //
      // This previous becomes possible.
      //
      fred.contacts must contain(barney)
    }

    "accessing the outer instance" in {
      class Network(val name: String) { outer => // <---- here is the alias 'outer' (could be any name) that will be used for accessing
        //                              the 'this' instance of 'Network'.

        class Member(val name: String) {
          val contacts = new mutable.ArrayBuffer[Network#Member]
          def description = s"$name inside ${outer.name}."
        }

        private val members = new mutable.ArrayBuffer[Network#Member] // <--- keeping an array of 'member of any network'.

        def join(name: String): Member = {
          val m = new Member(name)
          members += m
          m
        }
      }

      val chatter = new Network("Chatter")
      val myFace = new Network("MyFace")

      val fred = chatter join "Fred"
      val wilma = chatter join "Wilma"

      val barney = myFace join "Barney"
      val betty = myFace join "Betty"

      fred.description must be equalTo "Fred inside Chatter."
      wilma.description must be equalTo "Wilma inside Chatter."
      barney.description must be equalTo "Barney inside MyFace."
      betty.description must be equalTo "Betty inside MyFace."
    }
  }

  //
  //  Exercises
  //

  // todo: Exercises of chapter 5.
}