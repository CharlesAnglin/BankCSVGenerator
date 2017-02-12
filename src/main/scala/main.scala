package bank

object CSVDemo extends App with Utils {

  //most recent files first
  val files = Stream("Statements09012896268044.txt")

  completeCSV(files)
//  perMonthCSV(files)
//  monthlyAverage(files)

}