import Utils.Utils
import bank._
import org.joda.time.DateTime
import org.scalatest.{FlatSpec, MustMatchers}

class UtilsSpec extends FlatSpec with MustMatchers with Utils {

  val tran = Trans(new DateTime("2016-02-01"), "CARD PAYMENT TO ASDA STORES", -3.00, 0, Shopping())

    val trans2 = Stream(
      tran.copy(date = new DateTime("2016-03-03"), balance = -306.00),
      tran.copy(date = new DateTime("2016-03-02"), amount = -100, descType = Savings(), balance = -303.00),
      tran.copy(date = new DateTime("2016-04-02"), balance = -203.00),
      tran.copy(date = new DateTime("2016-04-01"), description = "CHEQUE PAID IN", amount = 9.00, descType = Ignored(), balance = -200.00),
      tran.copy(date = new DateTime("2016-03-03"), balance = -209.00),
      tran.copy(date = new DateTime("2016-03-02"), amount = -100, descType = Savings(), balance = -206.00),
      tran.copy(date = new DateTime("2016-03-01"), amount = 200, descType = Savings(), balance = -106.00),
      tran.copy(date = new DateTime("2016-02-03"), balance = -306.00),
      tran.copy(date = new DateTime("2016-02-02"), balance = -303.00),
      tran.copy(amount = -300.00, descType = Savings(), balance = -300.00),
      tran.copy(date = new DateTime("2016-01-29"), balance = 0)
    )

    "filterSavings" must
      "change the balance of each transaction to filter out savings" in {
      val result = filterSavings(trans2)

      println(result)

      result.size mustBe 6
      result.map(_.balance) mustBe Stream(-6.00, -3.00, 0.00, 3.00, 6.00, 9.00)
    }



}
