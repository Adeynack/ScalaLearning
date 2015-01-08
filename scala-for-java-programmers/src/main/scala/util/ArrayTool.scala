package util

abstract class ArrayToolBase[T] {
  def underlying: Array[T]
  def linearFill() : Unit
  implicit def toUnderlying: Array[T] = underlying
}

case class ArrayToolForArrayOfInt(underlying: Array[Int]) extends ArrayToolBase[Int] {
  override def linearFill() : Unit = {
    val value = new AutoIncrementInt
    for (i <- 0 until underlying.length) {
      underlying(i) = value next
    }
  }
}

case class ArrayToolForArrayOfArrayOfInt(underlying: Array[Array[Int]]) extends ArrayToolBase[Array[Int]] {
  override def linearFill(): Unit = {
    val value = new AutoIncrementInt
    for (r <- 0 until underlying.length; c <- 0 until underlying(r).length) {
      underlying(r)(c) = value next
    }
  }
}

object ArrayTool {
  implicit def fromArray(a: Array[Int]): ArrayToolBase[Int] = ArrayToolForArrayOfInt(a)
  implicit def fromArray(a : Array[Array[Int]]) : ArrayToolBase[Array[Int]] = new ArrayToolForArrayOfArrayOfInt(a)
}
