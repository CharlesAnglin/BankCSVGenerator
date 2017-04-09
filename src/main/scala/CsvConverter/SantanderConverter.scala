package CsvConverter

import java.nio.charset.CodingErrorAction
import bank.Transaction
import scala.io.{Codec, Source}

object SantanderConverter extends Converter {

  override def csvConverter(file: String): Stream[Transaction] = {
    val decoder = Codec.UTF8.decoder.onMalformedInput(CodingErrorAction.IGNORE)
    val source = Source.fromFile(file)(decoder).getLines().toList

    source
      .drop(4)
      .map(_.trim)
      .flatMap(string => if (string == "") None else Some(string))
      .sliding(4, 4)
      .map(list => Transaction(dateConverter(list(0)), descriptionConverter(list(1)), amountConverter(list(2)), balanceConverter(list(3))))
      .toStream
  }

  def dateConverter(date: String): String = {
    val pattern = "\\d{2}/\\d{2}/\\d{4}".r
    pattern.findFirstIn(date).getOrElse("no date found")
  }

  def amountConverter(amount: String): String = {
    val pattern = "[-]{0,1}\\d+\\.\\d+".r
    pattern.findFirstIn(amount).getOrElse("no amount found")
  }

  def balanceConverter(balance: String): String = {
    val pattern = "\\d+\\.\\d+".r
    pattern.findFirstIn(balance).getOrElse("no balance found")
  }

  def descriptionConverter(description: String): String = {
    val pattern = "[a-zA-Z .]+[^ \\d\\W]".r
    //"Description" is matched first
    val matches = pattern.findAllIn(description).toList
    if (matches.length <= 1) {
      "no description found"
    } else {
      matches(1)
    }
  }

}
