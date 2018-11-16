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
    ???
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

