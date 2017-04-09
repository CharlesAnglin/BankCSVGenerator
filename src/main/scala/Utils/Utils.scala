package Utils

import CsvConverter.Converter
import TransactionConverter.TransactionConverter
import bank._
import org.joda.time.DateTime

trait Utils extends TransactionConverter {

  //Assumes first file is the most recent
  def createInput(files: Map[String, Converter]): Stream[Trans] = {
    val initialConversion = files.flatMap { tup =>
      tup._2.csvConverter(tup._1)
    }.toStream

    transactionConverter(initialConversion) match {
      case Right(r) => r
      case Left(l) => println(Console.RED + "TRANSACTION CONVERTER FAILED ON: \n" + l + Console.RESET);
        Stream(Trans(new DateTime("2017"), "FAILURE", 0.00, 0.00, Unmatched()))
    }
  }

  //assumes most recent transactions are at the start
  def filterSavings(trans: Stream[Trans]): Stream[Trans] = {
    val savingsBalance = trans.foldLeft(0.0)((a, b) => if (b.descType == Savings()) {
      a + b.amount
    } else a)
    //add on the money that has gone out to savings
    val currentBalance = trans.head.balance - savingsBalance

    val filteredTrans = trans.filter(b => !List(Ignored(), Removed(), Savings()).contains(b.descType))

    def helperFunct(start: Stream[Trans], end: Stream[Trans]): Stream[Trans] = {
      if (end.isEmpty) {
        start
      } else {
        val newElement = end.head.copy(balance = start.last.balance - start.last.amount)
        helperFunct(start #::: Stream(newElement), end.tail)
      }
    }

    helperFunct(Stream(filteredTrans.head.copy(balance = currentBalance)), filteredTrans.tail)
  }

  /*
  * Used specifically for getPerMonthTransactions
  */
  //assumes most recent transactions are at the start
  def balancePerMonth(trans: Stream[Trans]): Stream[(DateTime, Double)] = {
    trans
      .reverse
      .sliding(2, 1)
      .toStream
      .flatMap { tup =>
        if (tup.last.descType == Salary()) {
          Some(tup.head.date, tup.head.balance)
        } else {
          None
        }
      }
      .reverse
  }

  /*
  * Used specifically for getMonthlyAverage
  */
  //ignores current month
  def monthSplit(trans: Stream[Trans]): Map[DateTime, Stream[Trans]] = {
    val transTail = trans.dropWhile(_.descType != Salary()).tail

    def recursiveHelper(trans: Stream[Trans], accumulator: Map[DateTime, Stream[Trans]] = Map()): Map[DateTime, Stream[Trans]] = {
      val index = trans.indexWhere(_.descType == Salary())
      if (index == -1) {
        accumulator
      } else {
        val (month, transSplit) = trans.splitAt(index + 1)
        recursiveHelper(transSplit, accumulator + (month.last.date -> month))
      }
    }

    recursiveHelper(transTail)
  }

  def descTypeBreakdown(trans: Stream[Trans]): Stream[Trans] = {
    trans.groupBy(_.descType)
      .values
      .toStream
      .map { descType =>
        descType.reduceLeft((a: Trans, b: Trans) => Trans(a.date, "", a.amount + b.amount, 0, a.descType))
      }
  }

  //assumes only one type of descType exists in each inner stream
  def averageByMonth(trans: Stream[Stream[Trans]]): Map[DescType, Double] = {
    descTypes
      .values
      .toStream
      .filter(!List(Savings(), Ignored(), Removed()).contains(_))
      .map { descType =>
        val average = trans.flatMap(descTypeBreakdown =>
          descTypeBreakdown.find(_.descType == descType)
        ).map(_.amount)
        if (average.isEmpty) {
          (descType, 0.0)
        } else {
          (descType, average.sum / average.size)
        }
      }.toMap
  }


}