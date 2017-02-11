package bank

import java.io.File

import com.github.tototoshi.csv.CSVWriter

trait MonthUtils extends TransactionUtils {



  //untested

  def outputCSV(transactions: Stream[Trans]) = {
    val f = new File("output.csv")
    val writer = CSVWriter.open(f)

    transactions.foreach{trans =>
      writer.writeRow(List(trans.date.toString.take(10), trans.description, trans.amount, trans.balance, trans.descType.toString.dropRight(2)))
    }

    writer.close()
  }

}
