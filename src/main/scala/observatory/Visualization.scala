package observatory

import com.sksamuel.scrimage.{ Image, Pixel }

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location     Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Temperature)], location: Location): Temperature = {
    def p(d: Double): Double = d * d

    temperatures.find(t => location.distance(t._1) <= 1) match {
      case Some(matched) =>
        matched._2
      case None =>
        val (numerator, denominator) = temperatures.foldLeft((0.0, 0.0)) { (acc, x) =>
          val w = 1 / p(location.distance(x._1))
          (acc._1 + w * x._2, acc._1 + w)
        }
        numerator / denominator
    }
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value  The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Temperature, Color)], value: Temperature): Color = {
    import scala.math._
    def interp(p1: Double, p2: Double, t: Double): Int = round((1 - t) * p1 + t * p2).toInt

    points.find(_._1 == value) match {
      case Some(matched) =>
        matched._2
      case None =>
        val (lower, higher) = points.toList.sortBy(_._1).partition(_._1 < value)
        (lower, higher) match {
          case (Nil, _) =>
            higher.head._2
          case (_, Nil) =>
            lower.last._2
          case (_, _) =>
            val above = higher.head
            val below = lower.last
            val t = abs(above._1 - value) / abs(above._1 - below._1)
            Color(
              interp(above._2.red, below._2.red, t),
              interp(above._2.green, below._2.green, t),
              interp(above._2.blue, below._2.blue, t)
            )
        }
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)]): Image = {
    ???
  }

}

