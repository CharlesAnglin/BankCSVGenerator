package bank

import org.joda.time.DateTime

case class Transaction(date: String, description: String, amount: String, balance: String)

case class Trans(date: DateTime, description: String, amount: Double, balance: Double, descType: DescType)
