import CsvConverter.SantanderConverter._
import bank.Transaction
import org.scalatest.{FlatSpec, MustMatchers}

class SantanderConverterSpec extends FlatSpec with MustMatchers {

  "csvConverter" must
    "return a stream of converted transaction documents" in {
    val result = csvConverter("TestStatement.txt")
    result.size mustBe 6
    result.head mustBe Transaction("09/02/2017", "CARD PAYMENT TO ASDA STORES", "-3.23", "1639.71")
    result.last mustBe Transaction("22/12/2016", "BANK GIRO CREDIT REF QA CONSULTING SERV", "1491.24", "130.76")
  }

  "dateConverter" must
    "return the date alone in the format dd/mm/yyyy" in {
    val result = dateConverter("Date:�09/02/2017")
    result mustBe "09/02/2017"
  }
  it must "return `no date found` for an improperly formatted date" in {
    val result = dateConverter("Date:�9/02/2017")
    result mustBe "no date found"
  }

  "amountConverter" must
    "return a negative amount in the correct format" in {
    val result = amountConverter("Amount:�-2.55�")
    result mustBe "-2.55"
  }
  it must "return a positive amount in the correct format" in {
    val result = amountConverter("Amount:�22.55�")
    result mustBe "22.55"
  }
  it must "return `no amount found` for an improperly formatted amount" in {
    val result = amountConverter("Amount:")
    result mustBe "no amount found"
  }

  "balanceConverter" must
    "return a balance in the correct format" in {
    val result = balanceConverter("Balance:�13642.94�")
    result mustBe "13642.94"
  }
  it must "return `no balance found` for an improperly formatted balance" in {
    val result = balanceConverter("Balance:")
    result mustBe "no balance found"
  }

  "descriptionConverter" must
    "return a letter only description and not `Description:`" in {
    val result = descriptionConverter("Description:�ASDA123�")
    result mustBe "ASDA"
  }
  it must "return `no description found` for an improperly formatted description" in {
    val result = descriptionConverter("Description:")
    result mustBe "no description found"
  }

}
