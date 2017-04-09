package Utils

import CsvConverter.Converter

trait Functionality extends Output with Utils {

  def getCompleteTransactions(files: Map[String, Converter]) = {
    val convertedData = createInput(files)
    val analysedData = filterSavings(convertedData)
    outputCompleteTransactionCSV(analysedData)
  }

}
