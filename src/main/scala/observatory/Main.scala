package observatory

object Main extends App {

  val temperatures = Extraction.locateTemperatures(1975, "/stations.csv", "/1975.csv")
  val averages = Extraction.locationYearlyAverageRecords(temperatures)
  val colors = Seq(
    (60.0, Color(255,255,255)),
    (32.0, Color(255,0,0)),
    (12.0, Color(255,255,0)),
    (0.0, Color(0,255,255)),
    (-15.0, Color(0,0,255)),
    (-27.0, Color(255,0,255)),
    (-50.0, Color(33,0,107)),
    (-60.0, Color(0,0,0)))

  println("calculated averages, visualizing now")

  val image = Visualization.visualize(averages, colors)

  println("saving image to disk")

  image.output(s"target/${System.currentTimeMillis()}.png")

  println("shutting down")

  SparkHelper.shutdown()

}
