package bank

trait descTypeMappings {

  val descTypes = Map(
    Vector("BOOTS", "ASDA", "SAINSBURY", "ICELAND", "MORRISON", "MARKS", "KIX", "GREGGS", "MCDONALDS", "ALDI", "WILKO", "SPA", "BHS") ->
      Shopping(),
    Vector("AMAZON") ->
      Amazon(),
    Vector("SPORTSDIRECT", "SPORTS", "RIVER", "NEXT", "TOP", "PRIMARK", "JD SPORTS") ->
      ClothesShopping(),
    Vector("THE GOLDEN EAGLE", "THE EASTFIELD INN", "ALBERT LOUNGE BAR", "HARVESTER", "CLIMBING", "NANDO", "TENPIN", "INN", "WHITE HARTE", "CINEWORLD", "ODEON", "MONTGOMERY", "THOMAS BOTFIELD", "GRAZING COW", "PARK INN") ->
      Social(),
    Vector("TRAIN", "LONDON MIDLAND", "MIDLAND", "TRAINLINE") ->
      Travel(),
    Vector("FOR COMMS", "INTEREST") ->
      MiscIn(),
    Vector("ACCOUNT FEE", "OXFAM", "CASH WITHDRAWAL", "WH SMITH", "EE", "POST OFFICE") ->
      MiscOut(),
    Vector("GPS") ->
      Rent(),
    Vector("CHARLIE HELP", "FROM ANGLIN", "CHARLES", "CLOSED ACCOUNT", "CHARLIE TESCO", "CHARLES REFERENCE INITIAL SUM", "ACCOUNT SAVING", "MR CHARLES DEVLIN ANGLIN REFERENCE", "INITIAL SUM", "REF. SANTANDER") ->
      Savings(),
    Vector("QA CONSULTING") ->
      Salary(),
    Vector("CHEQUE PAID IN") -> //N.B. ALL cheques will be ignored
      Ignored(),
    Vector("FASTER PAYMENTS RECEIPT REF.FSCB") ->
      Removed()
  )

}
