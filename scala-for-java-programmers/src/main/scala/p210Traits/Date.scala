package p210Traits

class Date(y: Int, m: Int, d: Int) extends Ord {

  def year = y

  def month = m

  def day = d

  override def toString: String = year + "-" + month + "-" + day

  override def equals(that: Any): Boolean =
    that.isInstanceOf[Date] && {
      val o = that.asInstanceOf[Date]
      o.day == day && o.month == month && o.year == year
    }

  def <(that: Any): Boolean = {
    if (!that.isInstanceOf[Date]) {
      sys.error("Cannot compare " + that + " and a Date")
    }

    val o = that.asInstanceOf[Date]

    (year < o.year) ||
      (year == o.year && (month < o.month ||
        (month == o.month && day < o.day)))
  }

  def apply(part: Char) = {
    val p : Char = part.toLower
    if (p == 'y')
      year
    else if (p == 'm')
      month
    else if (p == 'd')
      day
    else
      sys.error("Invalid part character.")
  }
}

object Date {

  def apply(y: Int, m: Int, d: Int) : Date = new Date(y, m, d)

  def apply(d: Int) = int2yearDate(d)

  implicit def int2yearDate(d: Int): Date = {
    // example: d = 20141229
    val day : Int = d % 100 // = 29
    var year = d / 100 // = 201412
    val month : Int = year % 100 // = 12
    year /= 100 // = 2014
    new Date(year, month, day) // 2014-12-29
  }

}
