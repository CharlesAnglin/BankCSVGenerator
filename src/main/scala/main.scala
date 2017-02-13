package bank

object CSVDemo extends App with Utils {

  //most recent files first
//  val files = Stream("NewSantanderStatements09012896268044.txt", "LloydsStatement21.txt")
  val files = Stream("NewSantanderStatements09012896268044.txt")

  completeCSV(files)
//  perMonthCSV(files)
//  monthlyAverage(files)

//  descriptionAnalyser(descriptionPull("NewSantanderStatements09012896268044.txt")).map(println)




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