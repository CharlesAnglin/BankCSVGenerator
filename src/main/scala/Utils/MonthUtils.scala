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
  def averageByMonth(trans: Stream[Stream[Trans]]): Map[DescType,Double] = {
    descTypes
      .values
      .toStream
      .map{descType =>
        val average = trans.flatMap(descTypeBreakdown =>
          descTypeBreakdown.find(_.descType == descType)
        ).map(_.amount)
        if(average.size == 0){
          (descType, 0.0)
        } else {
          (descType, average.sum / average.size)
        }
      }.toMap
  }


  //untested

  def outputCSV(transactions: Stream[Trans]) = {
    val f = new File("output.csv")
    val writer = CSVWriter.open(f)

    transactions.foreach { trans =>
      writer.writeRow(List(trans.date.toString.take(10), trans.description, trans.amount, trans.balance, trans.descType.toString.dropRight(2)))
    }

    writer.close()
  }

  def outputCSVAverage(average: Map[DescType,Double]) = {
    val f = new File("output.csv")
    val writer = CSVWriter.open(f)

    average.keys.foreach { descType: DescType =>
      writer.writeRow(List(descType.toString.dropRight(2), Math.round(average(descType)*100.0)/100.0))
    }

    writer.close()
  }

}
