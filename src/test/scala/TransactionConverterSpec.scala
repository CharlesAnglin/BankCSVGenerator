import CsvConverter.SantanderConverter.csvConverter
import TransactionConverter.TransactionConverter
import bank._
import org.joda.time.DateTime
import org.scalatest.{FlatSpec, MustMatchers}

class TransactionConverterSpec extends FlatSpec with MustMatchers with TransactionConverter {

  "initialConverter" must
    "convert a stream of Transaction classes into a stream of Trans classes" in {
    val result = initialConverter(csvConverter("TestStatement.txt"))
    result.isRight mustBe true
    result.right.get.length mustBe 6
    result.right.get.head mustBe Trans(new DateTime("2017-02-09"), "CARD PAYMENT TO ASDA STORES", -3.23, 1639.71, Unmatched())
  }
  it must "return the first Transaction it fails to convert" in {
    val result = initialConverter(csvConverter("TestStatement.txt") #::: Stream(Transaction("Date:�09/02/2017", "Description:�CARD PAYMENT TO ASDA STORES 5011,3.23 GBP, RATE 1.00/GBP ON 07-02-2017", "Amount:�-3.23�", "Balance:�1639.71�")))
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
    val transactionStream = initialConverter(csvConverter("TestStatement.txt")).right.get

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
