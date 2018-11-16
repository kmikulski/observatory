package observatory

import com.sksamuel.scrimage.{ Image, Pixel }

import scala.math._

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param tile Tile coordinates
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(tile: Tile): Location = coordinatesToLocation(tile.x, tile.y, tile.zoom)

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @param tile         Tile coordinates
    * @return A 256×256 image showing the contents of the given tile
    */
  def tile(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)], tile: Tile): Image = {
    val width, height = 256
    val pixels = for {
      y <- 0 until height
      x <- 0 until width
    } yield {
      val location = coordinatesToLocation(tile.x * width + x, tile.y * height + y, tile.zoom + 8)
      val temp = Visualization.predictTemperature(temperatures, location)
      val color = Visualization.interpolateColor(colors, temp)
      Pixel(color.red, color.green, color.blue, 127)
    }
    Image(width, height, pixels.toArray)
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    *
    * @param yearlyData    Sequence of (year, data), where `data` is some data associated with
    *                      `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Year, Data)],
    generateImage: (Year, Tile, Data) => Unit
  ): Unit = {
    ???
  }

  private def coordinatesToLocation(x: Double, y: Double, zoom: Int): Location = {
    val n = 2 ^ zoom
    val lon = x / n * 360 - 180
    val lat = atan(sinh(Pi * (1 - 2 * y / n))) * 180 / Pi
    Location(lat, lon)
  }

}
