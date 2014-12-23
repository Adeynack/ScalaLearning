import org.specs2.mutable.Specification

object TraitsSpec extends Specification {

  val dates = List(
    new Date(2014, 11, 22),
    new Date(2014, 11, 23),
    new Date(2014, 12, 22),
    new Date(2015, 12, 22))

  dates must have size 4
}
