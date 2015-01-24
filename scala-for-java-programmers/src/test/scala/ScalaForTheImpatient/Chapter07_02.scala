package com {
package horstmann {

object Utils {
  def percentOf(value: Double, rate: Double) = value * rate / 100.0
}

package impatient {

class Employee() {
  var salary: Double = 0.0

  def giveRaise(rate: scala.Double) {
    salary += Utils.percentOf(salary, rate)
  }
}

}

}

}
