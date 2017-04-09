package CsvConverter

import bank.Transaction

trait Converter {

  /*
  * Allows us to define multiple different converters as other banks might have a different layout to their CSV files.
  */

  def csvConverter(file: String): Stream[Transaction]

}
