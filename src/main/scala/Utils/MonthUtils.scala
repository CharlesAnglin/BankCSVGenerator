package bank

import java.io.File

import com.github.tototoshi.csv.CSVWriter
import org.joda.time.DateTime

trait MonthUtils extends TransactionUtils {

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
      .map { descType =>
        val average = trans.flatMap(descTypeBreakdown =>
          descTypeBreakdown.find(_.descType == descType)
        ).map(_.amount)
        if (average.size == 0) {
          (descType, 0.0)
        } else {
          (descType, average.sum / average.size)
        }
      }.toMap
  }

  //assumes most recent transactions are at the start
  def filterSavings(trans: Stream[Trans]): Stream[Trans] = {

    def recursiveHelper(trans: Stream[Trans]): Stream[Trans] = {
      val index = trans.indexWhere(tran => tran.descType == Savings() | tran.description.toUpperCase.contains("CHEQUE PAID IN"))
      if (index == -1) {
        return trans
      }
      val (head, tail) = trans.splitAt(index)
      val filteredTail = if (tail.head.descType == Savings()) {
        tail.map { trans =>
          trans.copy(balance = trans.balance - tail.head.amount)
        }
      } else {
        tail
      }
      val filteredHead = if (tail.head.descType == Savings()) {
        head
      } else {
        head.map { trans =>
          trans.copy(balance = trans.balance + tail.head.amount)
        }
      }
      recursiveHelper(filteredHead #::: filteredTail.tail)
    }

    recursiveHelper(trans.reverse).reverse
  }

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


  //untested

  //assumes first file is the most recent
  def createInput(inputFiles: Stream[String]): Stream[Trans] = {
    val converted = inputFiles.map { file =>
      TransactionConverter(textConverter(file))
    }.flatMap { either =>
      if (either.isLeft) {
        println(Console.RED + "TRANSACTION CONVERTER FAILED ON: \n" + either.left.get + Console.RESET)
        //passed back to keep types consistent
        Stream(Trans(new DateTime("2017"), "FAILURE", 0.00, 0.00, Unmatched()))
      } else {
        either.right.get
      }
    }

    filterSavings(DescriptionTypeSet(converted))
  }

  def outputCSV(transactions: Stream[Trans]) = {
    val f = new File("output.csv")
    val writer = CSVWriter.open(f)

    writer.writeRow(List("Date", "Description", "Amount", "Balance", "Type"))

    transactions.foreach { trans =>
      writer.writeRow(List(trans.date.toString.take(10), trans.description, trans.amount, trans.balance, trans.descType.toString.dropRight(2)))
    }

    writer.close()
  }

  def outputCSVAverage(average: Map[DescType, Double]) = {
    val f = new File("output.csv")
    val writer = CSVWriter.open(f)

    writer.writeRow(List("Type", "Average"))

    average.keys.foreach { descType: DescType =>
      writer.writeRow(List(descType.toString.dropRight(2), Math.round(average(descType) * 100.0) / 100.0))
    }

    writer.writeRow(List("Average sum", average.values.sum))

    writer.close()
  }

  def outputCSVPerMonth(point: Stream[(DateTime, Double)], date: Boolean = true) = {
    val f = new File("output.csv")
    val writer = CSVWriter.open(f)

    writer.writeRow(List("Date", "Balance"))

    point.foreach { point =>
      writer.writeRow(List(if (date) {
        point._1
      } else {
        point._1.getMillis / 1000
      }, point._2))
    }

    writer.close()
  }

}
