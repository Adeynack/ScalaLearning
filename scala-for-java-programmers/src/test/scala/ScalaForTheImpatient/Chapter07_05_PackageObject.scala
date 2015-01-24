package ScalaForTheImpatient

package object ThisIsAnExample {
  val FirstConstant = 1
  val SecondConstant = 2
  val ThirdConstant = 3
}

package ThisIsAnExample {

  class Something {
    var a = FirstConstant // available in all the package, since part of the 'package object'
    var b = SecondConstant
    var c = ThirdConstant
  }

  object Etwas {
    def f = {
      FirstConstant + SecondConstant + ThirdConstant
    }
  }

}
