/*
 * Copyright (c) 2014 Azavea.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geotrellis.engine.op.zonal

import geotrellis.raster._
import geotrellis.raster.mapalgebra.zonal._
import geotrellis.raster.histogram.Histogram
import geotrellis.engine._

import spire.syntax.cfor._

import scala.collection.mutable

@deprecated("geotrellis-engine has been deprecated", "Geotrellis Version 0.10")
trait ZonalRasterSourceMethods extends RasterSourceMethods {
  /**
   * Given a raster, return a histogram summary of the cells within each zone.
   *
   * @note    zonalHistogram does not currently support Double raster data.
   *          If you use a Raster with a Double CellType (FloatConstantNoDataCellType, DoubleConstantNoDataCellType)
   *          the data values will be rounded to integers.
   */
  def zonalHistogramInt(zonesSource: RasterSource): ValueSource[Map[Int, Histogram[Int]]] = {
    ValueSource(
      (rasterSource.convergeOp, zonesSource.convergeOp).map { (tile, zones) =>
        IntZonalHistogram(tile, zones)
      }
    )
  }

  /**
   * Given a raster, return a histogram summary of the cells within each zone.
   */
  def zonalHistogramDouble(zonesSource: RasterSource): ValueSource[Map[Int, Histogram[Double]]] = {
    ValueSource(
      (rasterSource.convergeOp, zonesSource.convergeOp).map { (tile, zones) =>
        DoubleZonalHistogram(tile, zones)
      }
    )
  }

  /**
   * Given a raster and a raster representing it's zones, sets all pixels
   * within each zone to the percentage of those pixels having values equal
   * to that of the given pixel.
   *
   * Percentages are integer values from 0 - 100.
   *
   * @note    ZonalPercentage does not currently support Double raster data.
   *          If you use a Raster with a Double CellType (FloatConstantNoDataCellType, DoubleConstantNoDataCellType)
   *          the data values will be rounded to integers.
   */
  def zonalPercentage(zonesSource: RasterSource): RasterSource =
    RasterSource(
      rasterSource.converge.combine(zonesSource.converge) { (tile, zones) =>
        ZonalPercentage(tile, zones)
      }.convergeOp,
      rasterSource.rasterDefinition.map(_.rasterExtent.extent)
    )
}
