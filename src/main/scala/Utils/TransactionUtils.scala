//package bank
//
//import java.nio.charset.CodingErrorAction
//import org.joda.time.DateTime
//import org.joda.time.format.DateTimeFormat
//import scala.io.{Codec, Source}
//import scala.util.Try
//
//trait TransactionUtils extends descTypeMappings {
//
//  //TODO: functions which begin with CAPTIAL letters. BAD!
//
//  /*
//  Used in conjunction with descriptionAnalyser to return descriptions along with their frequencies
//  i.e. `descriptionAnalyser(descriptionPull("Lloyds.txt")).map(println)`
//  Will print out a frequency analysis for a specific file.
//  */
//  def descriptionPull(file: String): Stream[String] = {
//    val decoder = Codec.UTF8.decoder.onMalformedInput(CodingErrorAction.REPLACE)
//    val source = Source.fromFile(file)(decoder).getLines().toList
//
//    source
//      .flatMap(string => if (string.startsWith("Description:")) Some(string.substring(13)) else None)
//      .map(_.trim)
//      .toStream
//  }
//
//  def descriptionAnalyser(descriptions: Stream[String]): Map[String, Int] = {
//    //find letters and spaces which are repeated but ignore any trailing spaces, digits or non letters
//    val pattern = "[a-zA-Z .]+[^ \\d\\W]".r
//    val filteredDescriptions = descriptions
//      .map(pattern.findFirstIn)
//      .map {
//        case Some(string) => string
//        case None => "FAILED REGEX PATTERN"
//      }
//      .groupBy(identity)
//      .map(pair => (pair._1, pair._2.length))
//    filteredDescriptions
//  }
//
//
//
//}
