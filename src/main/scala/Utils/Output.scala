package Utils

import java.io.File

import bank.{DescType, Trans}
import com.github.tototoshi.csv.CSVWriter
import org.joda.time.DateTime

trait Output {

  //  TODO: Completely untested - test create input to make sure it outputs in the right order?

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
