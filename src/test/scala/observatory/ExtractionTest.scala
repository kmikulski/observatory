package observatory

import java.time.LocalDate

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

trait ExtractionTest extends FunSuite {

  test("'locateTemperatures' should work for test data") {
    val expected = Set(
      (LocalDate.of(2015, 8, 11), Location(37.35, -78.433), 27.3),
      (LocalDate.of(2015, 12, 6), Location(37.358, -78.438), 0.0),
      (LocalDate.of(2015, 1, 29), Location(37.358, -78.438), 2.0)
    )
    val actual = Extraction.locateTemperatures(2015, "/stations.csv", "/readings.csv").toSet
    assert(expected == actual)
  }

  test("'locationYearlyAverageRecords' should work for test data") {
    val expected = Set(
      (Location(37.35, -78.433), 27.3),
      (Location(37.358, -78.438), 1.0)
    )
    val temps = Extraction.locateTemperatures(2015, "/stations.csv", "/readings.csv")
    val averages = Extraction.locationYearlyAverageRecords(temps)
    assert(expected == averages.toSet)
  }

}