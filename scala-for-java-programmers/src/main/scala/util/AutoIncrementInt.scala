package util

class AutoIncrementInt(private var current: Int = 0) {
  def next : Int = {
    val c = current
    current += 1
    c
  }
}
