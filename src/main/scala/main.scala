package bank

import java.io.File
import java.nio.charset.CodingErrorAction

import scala.io.{Codec, Source}
import com.github.tototoshi.csv._

object CSVDemo extends App with Utils {

  //most recent files first
  val files = Stream("Statements09012896268044.txt")

  completeCSV(files)
//  perMonthCSV(files)
//  monthlyAverage(files)

}