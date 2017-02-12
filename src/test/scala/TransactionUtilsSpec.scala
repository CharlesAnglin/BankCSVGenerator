package bank

import org.joda.time.DateTime
import org.scalatest._

class TransactionUtilsSpec extends FlatSpec with MustMatchers with TransactionUtils {

  "textConverter" must
    "return a stream of converted transaction documents" in {
    val result = textConverter("TestStatement.txt")
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

  "descriptionPull" must
    "return a stream of all the descriptions in a text file" in {
    val result = descriptionPull("TestStatement.txt")
    result.length mustBe 6
    result.head mustBe "CARD PAYMENT TO ASDA STORES 5011,3.23 GBP, RATE 1.00/GBP ON 07-02-2017"
  }

  "descriptionAnalyser" must
    "must format and return frequency analysis on a stream of descriptions" in {
    val result = descriptionAnalyser(descriptionPull("TestStatement.txt"))
    result.size mustBe 5
  }

  "TransactionConverter" must
    "convert a stream of Transaction classes into a stream of Trans classes" in {
    val result = TransactionConverter(textConverter("TestStatement.txt"))
    result.isRight mustBe true
    result.right.get.length mustBe 6
    result.right.get.head mustBe Trans(new DateTime("2017-02-09"), "CARD PAYMENT TO ASDA STORES", -3.23, 1639.71, Unmatched())
  }
  it must "return the first Transaction it fails to convert" in {
    val result = TransactionConverter(textConverter("TestStatement.txt") #::: Stream(Transaction("Date:�09/02/2017", "Description:�CARD PAYMENT TO ASDA STORES 5011,3.23 GBP, RATE 1.00/GBP ON 07-02-2017", "Amount:�-3.23�", "Balance:�1639.71�")))
    result.isLeft mustBe true
    result.left.get mustBe Transaction("Date:�09/02/2017", "Description:�CARD PAYMENT TO ASDA STORES 5011,3.23 GBP, RATE 1.00/GBP ON 07-02-2017", "Amount:�-3.23�", "Balance:�1639.71�")
  }

  "setType" must
    "return the right case class given a description" in {
    val result = List("123ASDA123",
      "ANEXTA",
      "amAzon",
      "",
      "THOMAS BOTFIELDaa",
      "THOMASaBOTFIELD").map(setType)

    result mustBe
      List(Shopping(),
        ClothesShopping(),
        Amazon(),
        Unmatched(),
        Social(),
        Unmatched()
      )
  }

  "DescriptionTypeSet" must
    "set a type for each transaction in a given stream of transactions" in {
    val transactionStream = TransactionConverter(textConverter("TestStatement.txt")).right.get

    val result = DescriptionTypeSet(transactionStream)

    result.map(_.descType) mustBe Stream(Shopping(), Shopping(), Shopping(), MiscIn(), MiscIn(), Salary())
  }
  it must "set MiscIn and MiscOut for description types which are unmatched" in {
    val trans = Trans(new DateTime("2017-02-09"), "blah", 1.5, 600, Unmatched())
    val result = DescriptionTypeSet(Stream(
      trans,
      trans.copy(amount = -1.5)
    ))

    result mustBe Stream(trans.copy(descType = MiscIn()), trans.copy(amount = -1.5, descType = MiscOut()))
  }
}
