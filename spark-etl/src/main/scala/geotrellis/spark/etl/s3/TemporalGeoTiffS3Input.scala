package geotrellis.spark.etl.s3

import geotrellis.raster.Tile
import geotrellis.spark.ingest._
import geotrellis.spark.io.s3._
import geotrellis.spark._
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD


class TemporalGeoTiffS3Input extends S3Input[TemporalProjectedExtent, Tile] {
  val format = "temporal-geotiff"
  def apply(props: Parameters)(implicit sc: SparkContext): RDD[(TemporalProjectedExtent, Tile)] =
    sc.newAPIHadoopRDD(configuration(props), classOf[TemporalGeoTiffS3InputFormat], classOf[TemporalProjectedExtent], classOf[Tile])
}
