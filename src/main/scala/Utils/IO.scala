package Utils

import bank.{DescType, Trans, Unmatched}
import java.io.File
import CsvConverter.Converter
import TransactionConverter.TransactionConverter
import com.github.tototoshi.csv.CSVWriter
import org.joda.time.DateTime

trait IO extends TransactionConverter {

  //  TODO: Completely untested - test create input to make sure it outputs in the right order?

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

  def outputCompleteTransactionCSV(transactions: Stream[Trans]) = {
    val f = new File("output.csv")
    val writer = CSVWriter.open(f)

    writer.writeRow(List("Date", "Description", "Amount", "Balance", "Type"))

    transactions.foreach {
      trans =>
        writer.writeRow(List(trans.date.toString.take(10), trans.description, trans.amount, trans.balance, trans.descType.toString.dropRight(2)))
    }

    writer.close()
  }

  def outputAverageCSV(average: Map[DescType, Double]) = {
    val f = new File("output.csv")
    val writer = CSVWriter.open(f)

    writer.writeRow(List("Type", "Average"))

    average.keys.foreach {
      descType: DescType =>
        writer.writeRow(List(descType.toString.dropRight(2), Math.round(average(descType) * 100.0) / 100.0))
    }

    writer.writeRow(List("Average sum", average.values.sum))

    writer.close()
  }

  def outputPerMonthCSV(point: Stream[(DateTime, Double)], date: Boolean = false) = {
    val f = new File("output.csv")
    val writer = CSVWriter.open(f)

    writer.writeRow(List("Date", "Balance"))

    point.foreach {
      point =>
        writer.writeRow(List(if (!date) {
          point._1
        } else {
          point._1.getMillis / 1000
        }, point._2))
    }

    writer.close()
  }

}
