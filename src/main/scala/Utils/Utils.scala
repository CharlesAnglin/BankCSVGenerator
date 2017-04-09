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
