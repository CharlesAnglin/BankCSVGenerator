//package bank
//
//import org.joda.time.DateTime
//import org.scalatest._
//
//class TransactionUtilsSpec extends FlatSpec with MustMatchers with TransactionUtils {
//
//
//  "descriptionPull" must
//    "return a stream of all the descriptions in a text file" in {
//    val result = descriptionPull("TestStatement.txt")
//    result.length mustBe 6
//    result.head mustBe "CARD PAYMENT TO ASDA STORES 5011,3.23 GBP, RATE 1.00/GBP ON 07-02-2017"
//  }
//
//  "descriptionAnalyser" must
//    "must format and return frequency analysis on a stream of descriptions" in {
//    val result = descriptionAnalyser(descriptionPull("TestStatement.txt"))
//    result.size mustBe 5
//  }
//
//}
