package ScalaForTheImpatient

import org.specs2.mutable.Specification

/**
 *
 *    Scala for the Impatient
 *
 *    Chapter 6: Objects
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
          lastNumber += 1; lastNumber
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

      a1 balance must be equalTo

    }
  }
}
