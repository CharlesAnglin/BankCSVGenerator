package bank

object CSVDemo extends App with Utils {

  //most recent files first
//  val files = Stream("NewSantanderStatements09012896268044.txt")
  val files = Stream("17-03-15Statements09012896268044.txt", "Lloyds.txt")
//  val files = Stream("Lloyds.txt")

//  completeCSV(files)
//  perMonthCSV(files)
  monthlyAverage(files)

//  descriptionAnalyser(descriptionPull("Lloyds.txt")).map(println)

//  val a = createInput(files).filter(_.descType==MiscIn()).toList.map(x => println(x.description))


  //TODO: make a function which prints of the desc type of everything in MiscIn()  (or better, of a chossen type?)
  //TODO: make an ignore type? Add in account creation and "cheque paid in type"?
  //TODO: make a function which limits the output to a certain time range?
  //TODO: have the three outputs out three CSV files of different names at the same time

  //create and send chart to plotly account
//  implicit val server = new writer.Server {
//    val credentials = writer.Credentials("brunch", "hKIhRk47FFSIOURwQxnu")
//    val url = "https://api.plot.ly/v2/"
//  }
//
//  val xs = (0.0 to 2.0 by 0.1)
//  val ys = xs.map { x => x*x }
//
//  val plot = Plot().withScatter(xs, ys)
//
//  draw(plot, "my-first-plot")


}