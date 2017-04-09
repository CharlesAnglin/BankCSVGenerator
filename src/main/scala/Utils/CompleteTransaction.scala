package Utils

import CsvConverter.Converter
import bank.Trans

trait CompleteTransaction extends IO {

  def getCompleteTransactions(files: Map[String, Converter]) = {
    val convertedData = createInput(files)
    val analysedData = filterSavings(convertedData)
    outputCompleteTransactionCSV(analysedData)
  }

  //assumes most recent transactions are at the start
  def filterSavings(trans: Stream[Trans]): Stream[Trans] = {
    ???
  }

}
