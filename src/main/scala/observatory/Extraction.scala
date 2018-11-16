package observatory

import java.time.LocalDate

import org.apache.spark.rdd.RDD

/**
  * 1st milestone: data extraction
  */
object Extraction {

  case class Id(stn: String, wban: String)
  case class Station(id: Id, location: Option[Location])
  case class Reading(id: Id, date: LocalDate, temp: Temperature)

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Year, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Temperature)] = {
    val stations: RDD[Station] = SparkHelper.read(stationsFile, stationFromCsv).filter(_.location.isDefined).cache
    val readings: RDD[Reading] = SparkHelper.read(temperaturesFile, readingFromCsv(year)).cache
    val joined: RDD[(Id, (Station, Reading))] = stations.map(s => (s.id, s)).join(readings.map(r => (r.id, r)))
    joined.map(j => (j._2._2.date, j._2._1.location.get, j._2._2.temp)).collect()
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Temperature)]): Iterable[(Location, Temperature)] = {
    records.groupBy(_._2).mapValues { i =>
      i.map(_._3).sum / i.size
    }
  }

  def stationFromCsv(csv: String): Station = {
    val b = csv.split(",", -1)
    val location = if(b(2).nonEmpty && b(3).nonEmpty) Some(Location(b(2).toDouble, b(3).toDouble)) else None
    Station(Id(b(0), b(1)), location)
  }

  def readingFromCsv(year: Year)(csv: String): Reading = {
    val b = csv.split(",", -1)
    val date = LocalDate.of(year, b(2).toInt, b(3).toInt)
    val temp = BigDecimal((b(4).toDouble - 32) / 1.8).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    Reading(Id(b(0), b(1)), date, temp)
  }

}
