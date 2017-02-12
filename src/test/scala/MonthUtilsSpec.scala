package bank

import org.joda.time.DateTime
import org.scalatest._

class MonthUtilsSpec extends FlatSpec with MustMatchers with MonthUtils {

  val tran = Trans(new DateTime("2016-02-01"), "CARD PAYMENT TO ASDA STORES", -3.00, 0, Shopping())

  val trans = Stream(
    tran.copy(date = new DateTime("2016-04-03")),
    tran.copy(date = new DateTime("2016-04-02")),
    tran.copy(date = new DateTime("2016-04-01"), description = "QA CONSULTING", amount = 1500.00, descType = Salary()),
    tran.copy(date = new DateTime("2016-03-03")),
    tran.copy(date = new DateTime("2016-03-02")),
    tran.copy(date = new DateTime("2016-03-01"), description = "QA CONSULTING", amount = 1300.00, descType = Salary()),
    tran.copy(date = new DateTime("2016-02-03")),
    tran.copy(date = new DateTime("2016-02-02")),
    tran.copy(description = "QA CONSULTING", amount = 1400.00, descType = Salary()),
    tran.copy(date = new DateTime("2016-01-29"))
  )

  val trans2 = Stream(
    tran.copy(date = new DateTime("2016-03-03"), balance = -306.00),
    tran.copy(date = new DateTime("2016-03-02"), amount = -100, descType = Savings(), balance = -303.00),
    tran.copy(date = new DateTime("2016-04-02"), balance = -203.00),
    tran.copy(date = new DateTime("2016-04-01"), description = "CHEQUE PAID IN", amount = 9.00, descType = MiscIn(), balance = -200.00),
    tran.copy(date = new DateTime("2016-03-03"), balance = -209.00),
    tran.copy(date = new DateTime("2016-03-02"), amount = -100, descType = Savings(), balance = -206.00),
    tran.copy(date = new DateTime("2016-03-01"), amount = 200, descType = Savings(), balance = -106.00),
    tran.copy(date = new DateTime("2016-02-03"), balance = -306.00),
    tran.copy(date = new DateTime("2016-02-02"), balance = -303.00),
    tran.copy(amount = -300.00, descType = Savings(), balance = -300.00),
    tran.copy(date = new DateTime("2016-01-29"), balance = 0)
  )

  "monthSplit" must
    "split a stream of transactions into chunks starting from the date of your monthly salary" in {
    val result = monthSplit(trans)

    result.size mustBe 2
    result.head mustBe(trans(5).date, trans.slice(3, 6))
    result.last mustBe(trans(8).date, trans.slice(6, 9))
  }

  "descTypeBreakdown" must
    "sum the amounts of a given stream of Trans by descType" in {
    val result = descTypeBreakdown(Stream(
      tran,
      tran.copy(amount = -100.00),
      tran.copy(descType = MiscIn()),
      tran.copy(amount = -30.00)))

    result.size mustBe 2
    result.map(_.amount) mustBe Stream(-133.00, -3.00)
  }

  "averageByMonth" must
    "average over each descType of a given stream of descTypeBreakdowns" in {
    val result = averageByMonth(Stream(
      Stream(tran,
        tran.copy(descType = Salary())),
      Stream(tran.copy(descType = Social()),
        tran.copy(amount = -9.00)
      )))

    result.size mustBe 10
    result(Social()) mustBe -3.00
    result(Salary()) mustBe -3.00
    result(Shopping()) mustBe -6.00
    result(MiscIn()) mustBe 0
  }

  "filterSavings" must
    "change the balance of each transaction to filter out savings" in {
    val result = filterSavings(trans2)

    result.size mustBe 6
    result.map(_.balance) mustBe Stream(-6.00, -3.00, 0.00, 3.00, 6.00, 9.00)
  }

  "balancePerMonth" must
  "return only a date balance pair for each month the day before salary is paied in" in {
    val result = balancePerMonth(trans)

    result mustBe Stream(
      (new DateTime("2016-03-03"), 0.0),
      (new DateTime("2016-02-03"), 0.0),
      (new DateTime("2016-01-29"), 0.0)
    )
  }

  "createInput" must
    "return a converted stream of transactions in the correct order" in {
    val result = createInput(Stream("TestStatement.txt", "TestStatement2.txt"))

    result.head.date mustBe new DateTime("2017-02-09")
    result.last.date mustBe new DateTime("2016-12-22")
  }

}
