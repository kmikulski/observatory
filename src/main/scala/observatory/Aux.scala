package observatory

import java.io.{ FileWriter, PrintWriter }

import org.apache.log4j.Logger

import scala.io.Source

// auxilliary class to shortlist main dataset to a random subset for testing
object Aux extends App {

  import Extraction._

  val stationLimit = 100
  val year = 2015

  val log = Logger.getLogger("Aux")

  log.info("reading stations")
  val stations = readStations("/stations.csv")
  log.info(s"stations read: ${stations.length}")
  val trimmedStations = scala.util.Random.shuffle(stations.filter(_.location.isDefined)).take(stationLimit)

  log.info(s"reading temperatures for $year")
  val temperatures = readTemperatures(s"/$year.csv", trimmedStations)
  log.info(s"temperature entries read: ${temperatures.length}")

  log.info("writing stations")
  writeStations("short-stations.csv", trimmedStations)
  log.info("writing temperatures")
  writeTemperatures("short-temperatures.csv", temperatures)

  log.info("shutting down")


  private def readStations(file: String): List[Station] = {
    val bufferedSource = Source.fromInputStream(getClass.getResourceAsStream(file))
    val stations = (for (line <- bufferedSource.getLines) yield stationFromCsv(line)).toList
    bufferedSource.close()
    stations
  }

  private def readTemperatures(file: String, stations: List[Station]): List[Reading] = {
    val stationIds = stations.map(_.id).toSet
    val bufferedSource = Source.fromInputStream(getClass.getResourceAsStream(file))
    val readings = (for {
      line <- bufferedSource.getLines
      reading = readingFromCsv(year)(line)
      if stationIds.contains(reading.id)
    } yield reading).toList
    bufferedSource.close()
    readings
  }

  private def writeStations(file: String, stations: Seq[Station]): Unit = {
    val bw = new PrintWriter(new FileWriter(file))
    stations.foreach(s => bw.println(s"${s.id.stn},${s.id.wban},${s.location.get.lat},${s.location.get.lon}"))
    bw.close()
  }

  private def writeTemperatures(file: String, temperatures: Seq[Reading]): Unit = {
    val bw = new PrintWriter(new FileWriter(file))
    temperatures.foreach { t =>
      val fahrenheit = BigDecimal(t.temp * 1.8 + 32).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
      bw.println(s"${t.id.stn},${t.id.wban},${t.date.getMonthValue},${t.date.getDayOfMonth},$fahrenheit")
    }
    bw.close()
  }
}
