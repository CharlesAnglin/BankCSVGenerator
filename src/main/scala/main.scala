package bank

import java.io.File
import java.nio.charset.CodingErrorAction

import scala.io.{Codec, Source}
import com.github.tototoshi.csv._

object CSVDemo extends App with MonthUtils {

  //    val decoder = Codec.UTF8.decoder.onMalformedInput(CodingErrorAction.IGNORE)
  //    val source = Source.fromFile("Statements09012896268044.txt")(decoder).getLines().toList
  //
  //
  //    val result = source.drop(4)
  //  //    .take(30)
  //      .map(_.trim)
  //      .flatMap(string => if (string == "") None else Some(string))
  //      .flatMap(string => if(string.startsWith("Date") || string.startsWith("Balance")) Some(string.split(":").last) else None)
  //      .map(_.split("GBP").head)
  //  //    .map(string => println(Console.YELLOW + "LINE: " + Console.RESET + string))
  //
  //
  //    val f = new File("output.csv")
  //    val writer = CSVWriter.open(f)
  //
  //    writer.writeAll(result.sliding(2,2).toList)
  //
  //    writer.close()

  //    val result = descriptionAnalyser(descriptionPull("Statements09012896268044.txt"))


  val trans = TransactionConverter(textConverter("Statements09012896268044.txt"))
  if(trans.isLeft){
    println("FAILURE converting:\n" + trans.left.get)
  } else {
    outputCSV(DescriptionTypeSet(trans.right.get))
  }

}