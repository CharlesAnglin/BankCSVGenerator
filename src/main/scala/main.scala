package bank

import java.io.File
import java.nio.charset.CodingErrorAction

import scala.io.{Codec, Source}
import com.github.tototoshi.csv._

object CSVDemo extends App with MonthUtils {

  val trans = DescriptionTypeSet(TransactionConverter(textConverter("Statements09012896268044.txt")).right.get)

//  if(trans.isLeft){
//    println("FAILURE converting:\n" + trans.left.get)
//  } else {
//
//    outputCSV(DescriptionTypeSet(trans.right.get))
//  }

  val averageByMonth: Map[DescType,Double] = averageByMonth(monthSplit(trans).values.map(descTypeBreakdown).toStream)

  outputCSVAverage(averageByMonth)

}