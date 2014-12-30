package p210TraitsTests

import org.specs2.mutable.Specification
import p210Traits.Date

/*

  https://gist.github.com/seratch/1414177

  http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html

*/

object DateOrdTraitSpec extends Specification {

  "A date" should {

    "be created using the 'apply' method of the companion object" in {
      val d: Date = Date(2014, 7, 8)
      d.year must be equalTo 2014
      d.month must be equalTo 7
      d.day must be equalTo 8
    }

    "be created from an int" in {
      val d: Date = Date(20141229)
      d.year must be equalTo 2014
      d.month must be equalTo 12
      d.day must be equalTo 29
    }

    "be converted from an int" in {
      val d: Date = 20141229
      d.year must be equalTo 2014
      d.month must be equalTo 12
      d.day must be equalTo 29
    }

  }

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

    "be accessible by character (date part)" in {
      today('y') must be equalTo 2014
      today('m') must be equalTo 11
      today('d') must be equalTo 22
      today('Y') must be equalTo 2014
      today('M') must be equalTo 11
      today('D') must be equalTo 22
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

  "when automatically tested by array index, dates" should {
    val dates: Array[Date] = Array(today, tomorrow, nextMonth, nextYear)
    for {leftIndex <- 0 until dates.length
         rightIndex <- 0 until dates.length} {
      val leftDate = dates(leftIndex)
      val rightDate = dates(rightIndex)
      def t(description: String): String = "%s %s %s".format(leftDate, description, rightDate)
      if (leftIndex < rightIndex) {
        t("< ") ! (leftDate < rightDate)
        t("<=") ! (leftDate <= rightDate)
        t("!=") in {
          leftDate must not equalTo rightDate
        }
      } else if (leftIndex == rightIndex) {
        t("<=") ! (leftDate <= rightDate)
        t("= ") in {
          leftDate must be equalTo rightDate
        }
        t(">=") ! (leftDate >= rightDate)
      } else {
        t("> ") ! (leftDate > rightDate)
        t(">=") ! (leftDate >= rightDate)
        t("!=") in {
          leftDate must not equalTo rightDate
        }
      }
    }
  }
}
