package bank

trait descTypeMappings {

  val descTypes = Map (
    Vector("BOOTS", "ASDA", "SAINSBURY", "ICELAND", "MORRISON", "MARKS", "KIX", "GREGGS", "MCDONALDS", "ALDI", "WILKO", "SPA") ->
      Shopping(),
    Vector("AMAZON") ->
      Amazon(),
    Vector("RIVER", "NEXT", "TOP", "PRIMARK") ->
      ClothesShopping(),
    Vector("CLIMBING", "NANDO", "TENPIN", "INN", "WHITE HARTE", "CINEWORLD", "ODEON", "MONTGOMERY", "THOMAS BOTFIELD", "GRAZING COW") ->
      Social(),
    Vector("TRAIN", "LONDON MIDLAND") ->
      Travel(),
    Vector("FOR COMMS", "INTEREST", "CHEQUE PAID IN") ->
      MiscIn(),
    Vector("ACCOUNT FEE", "OXFAM", "CASH WITHDRAWAL", "WH SMITH", "EE") ->
      MiscOut(),
    Vector("GPS") ->
      Rent(),
    Vector("CHARLIE TESCO", "CHARLES REFERENCE INITIAL SUM", "ACCOUNT SAVING", "MR CHARLES DEVLIN ANGLIN REFERENCE", "INITIAL SUM", "REF. SANTANDER") ->
      Savings(),
    Vector("QA CONSULTING") ->
      Salary()
    )

}
