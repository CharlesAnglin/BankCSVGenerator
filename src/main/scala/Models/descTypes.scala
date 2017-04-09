package bank

class DescType(description: String)

//TODO: change these to objects

case class Shopping() extends DescType("weekly shopping from ASDA, iceland etc, includes lunch bought from Boots, Subway etc")

case class Amazon() extends DescType("any purchases made from Amazon")

case class ClothesShopping() extends DescType("any purchases made from clothing shops")

case class Social() extends DescType("any purchases made from pubs or at events")

case class Travel() extends DescType("any purchases made from train companies")

case class MiscIn() extends DescType("Misc, includes refunds")

case class MiscOut() extends DescType("Misc, includes cash withdrawal, phone bills, bank charges")

case class Rent() extends DescType("Rent")

case class Savings() extends DescType("Money being moved for savings")

case class Salary() extends DescType("Salary")

case class Unmatched() extends DescType("Descriptions which could not be matched or has not been attempted - will later be sorted into MiscIn or MisOut")

case class Ignored() extends DescType("Transactions to be ignored - such as large cheques paid in.")

case class Removed() extends DescType("Transactions to be removed and not rebalanced - such as initial sum contributed when opening account.")

