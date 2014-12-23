package p100HelloWorld

////------------------------------------------------------------------------------------------------
//// Hello World example
////
//object HelloWorld { // "Singleton Object" syntax
//  def main(args: Array[String]) { // No return type = void return type (procedure).
//    println("Hello, world!")
//  }
//}


////------------------------------------------------------------------------------------------------
//// Extending the "App" trait makes the method the "main" like above.
////
//object HelloWorld extends App {
//  println("Hello, world!")
//}
//


////------------------------------------------------------------------------------------------------
//// Imports and infix syntax
////
//import java.util.{Date, Locale}
//import java.text.DateFormat
//
//object HelloWorld extends App {
//  val now = new Date
//  val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE)
//  val s = df format now // NB: infix syntax, equivalent to df.format(now)
//  println(s)
//}


////------------------------------------------------------------------------------------------------
//// Static imports
////
//import java.util.Date
//// the next 2 lines will imports everything static in DateFormat and Locale, allowing to stop
//// repeating the class name.
//import java.text.DateFormat._
//import java.util.Locale._
//
//object HelloWorld extends App {
//  val now = new Date
//  val df = getDateInstance(LONG, FRANCE)
//  val s = df format now
//  println(s)
//}


//================================================================================================
// Everything is an Object
//================================================================================================


//------------------------------------------------------------------------------------------------
// Functions are objects
//
//object HelloWorld {
//  def oncePerSecond(callback: () => Unit) {
//    while (true) { callback(); Thread sleep 1000 }
//  }
//  def timeFlies() {
//    println("time flies like an arrow...")
//  }
//  def main(args: Array[String]) {
//    oncePerSecond(timeFlies)
//  }
//}


////------------------------------------------------------------------------------------------------
//// Anonymous functions (aka: Lambda)
////
//// Same example as the one before, but with an anonymous function instead of 'timeFlies'.
////
//object HelloWorld {
//
//  def oncePerSecond(callback: () => Unit): Unit = {
//    while (true) {
//      callback()
//      Thread sleep 1000
//    }
//  }
//
//  def main(args: Array[String]): Unit = {
//    // NB: Instead of passing a function as a parameter, we pass an anonymous function (lambda) doing
//    // the same job.
//    //
//    // Lambda in Scala:
//    // () => something()
//    //
//    oncePerSecond(() => println("time flies like an arrow..."))
//  }
//
//}
