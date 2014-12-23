package p210TraitsTests

import org.specs2.mutable._
import p210Traits._

/*

  https://gist.github.com/seratch/1414177

  http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html

*/

object DateOrdTraitSpec extends Specification {

  val today = new Date(2014, 11, 22)
  val tomorrow = new Date(2014, 11, 23)
  val nextMonth = new Date(2014, 12, 22)
  val nextYear = new Date(2015, 12, 22)

  "Today (Nov. 22, 2014)" should {

    "be '2014-11-22' when converted to string" in {
      today.toString must be equalTo "2014-11-22"
    }

    "is not equal when compared to a string (str to Date conversation not supported)" in {
      today must not be equalTo("2014-11-22")
    }

    "throws an exception when checked if less to a string" in {
      def f: Boolean = today < "2014-11-22"
      f must throwA[Exception]
    }

    "be equal to itself" in {
      today must be equalTo today
    }

    "be less or equal to itself" ! (today <= today)
    "be greater or equal to itself" ! (today >= today)
    "be less than tomorrow" ! (today < tomorrow)
    "be less or equal than tomorrow" ! (today <= tomorrow)
    "be less than next month" ! (today < nextMonth)
    "be less or equal than next month" ! (today <= nextMonth)
    "be less than next year" ! (today < nextYear)
    "be less or equal than next year" ! (today <= nextYear)
  }

  "Tomorrow (Nov. 23, 2014)" should {

    "be '2014-11-23' when converted to string" in {
      tomorrow.toString must be equalTo "2014-11-23"
    }

    "is not equal when compared to a string (str to Date conversation not supported)" in {
      tomorrow must not be equalTo("2014-11-23")
    }

    "be greater than today" ! (tomorrow > today)
    "be greater than or equal to today" ! (tomorrow >= today)

    "be less than or equal to tomorrow" ! (tomorrow <= tomorrow)

    "be equal to itself" in {
      tomorrow must be equalTo tomorrow
    }

    "be less than or equal than tomorrow" ! (tomorrow >= tomorrow)

    "be less than next month" ! (tomorrow < nextMonth)
    "be less than or equal than next month" ! (tomorrow <= nextMonth)

    "be less than next year" ! (tomorrow < nextYear)
    "be less than or equal than next year" ! (tomorrow <= nextYear)
  }

  "Next month (Dev. 22, 2014)" should {

    "be '2014-12-22' when converted to string" in {
      nextMonth.toString must be equalTo "2014-12-22"
    }

    "is not equal when compared to a string (str to Date conversation not supported)" in {
      nextMonth must not be equalTo("2014-12-22")
    }

    "be greater than today" ! (nextMonth > today)
    "be greater or equal to today" ! (nextMonth >= today)

    "be greater than today" ! (nextMonth > tomorrow)
    "be greater or equal to today" ! (nextMonth >= tomorrow)

    "be less than or equal to itself" ! (nextMonth <= nextMonth)

    "be equal to itself" in {
      nextMonth must be equalTo nextMonth
    }

    "be less than or equal than itself" ! (nextMonth >= nextMonth)

    "be less than next year" ! (nextMonth < nextYear)
    "be less or equal than next year" ! (nextMonth <= nextYear)
  }

  "Next year (Dev. 22, 2015)" should {

    "be '2015-12-22' when converted to string" in {
      nextYear.toString must be equalTo "2015-12-22"
    }

    "is not equal when compared to a string (str to Date conversation not supported)" in {
      nextYear must not be equalTo("2015-12-22")
    }

    "be greater than today" ! (nextYear > today)
    "be greater or equal to today" ! (nextYear >= today)

    "be greater than today" ! (nextYear > tomorrow)
    "be greater or equal to today" ! (nextYear >= tomorrow)

    "be less than next month" ! (nextYear > nextMonth)
    "be less or equal than next month" ! (nextYear >= nextMonth)

    "be less than or equal to itself" ! (nextYear <= nextYear)

    "be equal to itself" in {
      nextYear must be equalTo nextYear
    }

    "be less than or equal than itself" ! (nextYear >= nextYear)
  }
}
