//================================================================================================
// Classes
//================================================================================================


//------------------------------------------------------------------------------------------------
// Introduction to classes
//

//// NB: It's possible to specify parameters directly to class. Those will be constructor
//// parameters.
//class Complex(real: Double, imaginary: Double) {
//  // 'Get' properties as method with zero arguments. Their type is automatically inferred from
//  // the construction parameters.
//  def re() = real
//  def im() = imaginary
//}
//
//object HelloWorld extends App {
//  val c = new Complex(0.2, 1.4)
//  println(c.re()) // calling getter with normal syntax
//  println(c im()) // calling getter with infix syntax.
//}
//


//------------------------------------------------------------------------------------------------
// Methods without arguments
//

//class Complex(real: Double, imaginary: Double) {
//  // 'Get' properties as methods without arguments.
//  def re = real
//  def im = imaginary
//}
//
//object HelloWorld extends App {
//  val c = new Complex(0.2, 1.4)
//  println(c re)
//  println(c im)
//}


//------------------------------------------------------------------------------------------------
// Inheritance and overriding
//
// All classes inherit from 'scala.AnyRef'.
//

//class Complex(real: Double, imaginary: Double) {
//  // 'Get' properties as methods without arguments.
//  def re = real
//
//  def im = imaginary
//
//  // NB: To override, simple precede 'def' with 'override'.
//  override def toString() = "" + re + (if (im < 0) "" else "+") + im + "i"
//
//  // NB: The previous definition is equivalent to this.
//  //  override def toString() {
//  //    return "" + re + (if(im < 0) "" else "+") + im + "i"
//  //  }
//}


//------------------------------------------------------------------------------------------------
// Case Classes and Pattern Matching
//
//
//abstract class Tree
//
//case class Sum(l: Tree, r: Tree) extends Tree {
//  override def toString = "(" + l.toString + " + " + r.toString + ")"
//}
//
//case class Var(n: String) extends Tree {
//  override def toString = n
//}
//
//case class Const(v: Int) extends Tree {
//  override def toString = v.toString
//}
//
//object HelloWorld {
//  type Environment = String => Int
//
//  def eval(t: Tree, env: Environment): Int = t match {
//    case Sum(l, r) => eval(l, env) + eval(r, env)
//    case Var(n) => env(n)
//    case Const(v) => v
//  }
//
//  def derive(t: Tree, v: String): Tree = t match {
//    case Sum(l, r) => Sum(derive(l, v), derive(r, v))
//    case Var(n) if v == n => Const(1)
//    case _ => Const(0)
//  }
//
//  def main(args: Array[String]) {
//    val exp: Tree = Sum(Sum(Var("x"), Var("x")), Sum(Const(7), Var("y")))
//    val env: Environment = {
//      case "x" => 5
//      case "y" => 7
//    }
//    println("Expression: " + exp)
//    println("Evaluation with x=5, y=7: " + eval(exp, env))
//    println("Derivative relative to x:\n " + derive(exp, "x"))
//    println("Derivative relative to y:\n " + derive(exp, "y"))
//  }
//}

