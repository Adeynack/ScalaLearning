case class Rational(numer: Int, denom: Int) {

  override def toString = s"$numer/$denom"

  def +(other: Rational) = Rational(
    (this.numer * other.denom) + (other.numer * this.denom),
    this.denom * other.denom
  )

  def -(other: Rational) = this + other.neg

  def *(other: Rational) = Rational(
    this.numer * other.numer,
    this.denom * other.denom
  )

  def /(other: Rational) = Rational(
    this.numer * other.denom,
    this.denom * other.numer
  )

  def neg = Rational(-numer, denom)

  def simplify = {
    val lower = if (numer < denom) numer else denom
    val higher = if (numer > denom) numer else denom
    // NB: This is a dumb implementation without any optimisation.
    def greaterCommonDivider(tentative: Int): Int = {
      if (lower % tentative == 0 && higher % tentative == 0) tentative
      else greaterCommonDivider(tentative - 1)
    }
    val gcd = greaterCommonDivider(lower)
    if (gcd > 1) Rational(numer / gcd, denom / gcd)
    else Rational(numer, denom)
  }
}

Rational(16, 32).simplify
Rational(17, 32).simplify
Rational(45, 46).simplify

Rational(1, 2) + Rational(2, 3).neg
Rational(1, 4) + Rational(2, 8)

Rational(1, 3) - Rational(5, 7) - Rational(3, 2)
