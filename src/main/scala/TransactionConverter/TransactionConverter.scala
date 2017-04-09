package TransactionConverter

import bank.{MiscOut, _}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.util.Try

trait TransactionConverter extends descTypeMappings {

  /*
  * Functions in this trait are here to transform Transaction case classes into Trans case classes.
  * E.g. matching a transaction to a description type.
  */

  /*
  * Returns either a converted stream or the first transaction which could not be converted.
  */
  def TransactionConverter(transactions: Stream[Transaction]): Either[Transaction, Stream[Trans]] = {
    //TODO: Try should return an option? So why wrap it in a either too?
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
