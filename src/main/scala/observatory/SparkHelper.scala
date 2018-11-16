package observatory

import java.nio.file.Paths

import org.apache.log4j.{ Level, Logger }
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.reflect.ClassTag

object SparkHelper {

  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  lazy val spark: SparkSession = SparkSession.builder()
    .appName("Observatory")
    .config("spark.master", "local")
    .getOrCreate()

  def read[T : ClassTag](resource: String, parser: String => T): RDD[T] =
    spark.sparkContext.textFile(fsPath(resource)).map(parser)

  def shutdown(): Unit = spark.stop()

  private def fsPath(resource: String): String = Paths.get(getClass.getResource(resource).toURI).toString
}
