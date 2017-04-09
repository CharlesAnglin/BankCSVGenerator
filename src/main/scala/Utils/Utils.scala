

//package bank
//
//trait Utils extends MonthUtils {
//
//  //untested
//
//  def completeCSV(inputFiles: Stream[String]) = {
//    val convertedInput = createInput(inputFiles)
//
//    outputCSV(convertedInput)
//  }
//
//  def perMonthCSV(inputFiles: Stream[String], unixTime: Boolean = false) = {
//    //gets input - createInput
//    //reduces each month into a single data point, the day before the salary is put in - balancePerMonth
//    //writes to file with with Joda date or unix time - outputCSVPerMonth
//    val convertedInput = createInput(inputFiles)
//
//    outputCSVPerMonth(balancePerMonth(convertedInput), unixTime)
//  }
//
//  //assumes most recent file first
//  def monthlyAverage(inputFiles: Stream[String]) = {
//    //gets input - createInput
//    //splits input into monthly chunks - monthSplit
//    //for each month reduces the transactions to one average - descTypeBreakdown
//    //takes the average over all months - averageByMonth
//    //writes to file - outputCSVAverage
//    val convertedInput = createInput(inputFiles)
//
//    val average = averageByMonth(monthSplit(convertedInput).values.map(descTypeBreakdown).toStream)
//
//    outputCSVAverage(average)
//  }
//
//}

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

}