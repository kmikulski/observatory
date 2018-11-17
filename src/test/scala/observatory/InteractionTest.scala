package observatory

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

trait InteractionTest extends FunSuite with Checkers {

  test("'generateTiles' should cover all expected tiles") {
    var acc = List[(Year, Tile, String)]()
    val yearlyData: Seq[(Year, String)] = Seq((2001, "first"), (2002, "second"))
    def generateImage(year: Year, tile: Tile, s: String): Unit = {
      acc = (year, tile, s) :: acc
    }
    Interaction.generateTiles(yearlyData, generateImage)
    assert(acc.size == 170)
  }

}
