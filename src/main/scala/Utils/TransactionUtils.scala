package bank

import java.nio.charset.CodingErrorAction

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.io.{Codec, Source}
import scala.util.Try


trait TransactionUtils extends descTypeMappings {

  def textConverter(file: String): Stream[Transaction] = {
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
    //    pattern.findFirstIn(description.replace("Description:", "")).getOrElse("no description found")
  }

  def descriptionPull(file: String): Stream[String] = {
    val decoder = Codec.UTF8.decoder.onMalformedInput(CodingErrorAction.REPLACE)
    val source = Source.fromFile(file)(decoder).getLines().toList

    source
      .flatMap(string => if (string.startsWith("Description:")) Some(string.substring(13)) else None)
      .map(_.trim)
      .toStream
  }

  def descriptionAnalyser(descriptions: Stream[String]): Map[String, Int] = {
    //find letters and spaces which are repeated but ignore any trailing spaces, digits or non letters
    val pattern = "[a-zA-Z .]+[^ \\d\\W]".r
    val filteredDescriptions = descriptions
      .map(pattern.findFirstIn)
      .map {
        case Some(string) => string
        case None => "FAILED REGEX PATTERN"
      }
      .groupBy(identity)
      .map(pair => (pair._1, pair._2.length))
    filteredDescriptions
  }

  def TransactionConverter(transactions: Stream[Transaction]): Either[Transaction, Stream[Trans]] = {
    val convertedTransactions = transactions.map { trans =>
      Try {
        Right(
          Trans(
            DateTime.parse(trans.date, DateTimeFormat.forPattern("dd/MM/yyyy")),
            trans.description,
            trans.amount.toDouble,
            trans.balance.toDouble,
            Unmatched()
          )
        )
      }.getOrElse(Left(trans))
    }

    val firstFailure = convertedTransactions.dropWhile(_.isRight)

    if (firstFailure.isEmpty) {
      Right(convertedTransactions.map(_.right.get))
    } else {
      Left(firstFailure.head.left.get)
    }
  }

  def setType(description: String): DescType = {
    val desc = description.toUpperCase
    val keys = descTypes.keySet
    val trueFalse = keys.flatMap { vector =>
      vector.flatMap { x =>
        if (desc.contains(x)) Some(descTypes(vector)) else None
      }
    }
    if (trueFalse.isEmpty) Unmatched() else trueFalse.head
  }

  def DescriptionTypeSet(transactions: Stream[Trans]): Stream[Trans] = {
    transactions.map { trans =>
      trans.copy(
        descType = setType(trans.description)
      )
    }.map { trans =>
      trans.descType match {
        case Unmatched() => if (trans.amount > 0) trans.copy(descType = MiscIn()) else trans.copy(descType = MiscOut())
        case _ => trans
      }
    }
  }


}
