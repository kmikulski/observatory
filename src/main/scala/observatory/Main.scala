package observatory

object Main extends App {

  val temps = Extraction.locateTemperatures(1975, "/stations.csv", "/1975.csv")
  val avgs = Extraction.locationYearlyAverageRecords(temps)

  avgs.take(20).foreach(println)

  SparkHelper.shutdown()

}
