package Utils

import CsvConverter.Converter

trait Functionality extends Output with Utils {

  //TODO: add do all funct
  //TODO: sort out package structure

  def getCompleteTransactions(files: Map[String, Converter]) = {
    val convertedData = createInput(files)
    val analysedData = filterSavings(convertedData)
    outputCompleteTransactionCSV(analysedData)
  }

  def getPerMonthTransactions(files: Map[String, Converter]) = {
    val convertedData = createInput(files)
    val analysedData = balancePerMonth(filterSavings(convertedData))
    outputPerMonthCSV(analysedData)
  }

}
