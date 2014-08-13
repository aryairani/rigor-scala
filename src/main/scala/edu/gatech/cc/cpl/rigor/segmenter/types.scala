package edu.gatech.cc.cpl.rigor.segmenter

import breeze.linalg.DenseMatrix
import edu.gatech.cc.cpl.rigor._
import edu.gatech.cc.cpl.rigor.regression._

import scalaz.State

/**
 * Created by arya on 7/30/14.
 */
case class SuperpixelIdx(i: Int) extends AnyVal
case class Index[Tag](i: Int) extends AnyVal
case class Pixel1DIdx(i: Int) extends AnyVal
case class EdgeletIdx(i: Int) extends AnyVal
case class Edgelet(sp1: SuperpixelIdx, sp2: SuperpixelIdx)
case class BoundaryPair(p1: Pixel1DIdx, p2: Pixel1DIdx)
//  case class BoundaryPairItensity(bp: BoundaryPair, intensity: BoundaryStrength)
//  case class EdgeletPlus(edgelet: Edgelet, boundaryPair: Seq[BoundaryPair])
//  case class EdgeletPlusIntensity(edgelet: Edgelet, boundaryPairIntensity: Seq[BoundaryPairItensity])
// (Edgelet, Map[BoundaryPair,BoundaryStrength])


//  case class Segmentation(image: LabelMatrix, labels: Seq[Label])

case class SuperPixel(area: Int, centroid: Coord, colorMean: Float3, colorStdDev: Float3)

case class SuperPixelData(seg: Segmentation, info: Seq[SuperPixel])
case class SegObj(spData: SuperPixelData
                  //                    ,sp_frame_set: ???
                  //                    ,edge_vals: ???
                  //                    ,precomputed_seeds: ???
                  //                    ,cut_segs: ???
                  //                    ,num_segs: ???
                  //                    ,meta_cut_segs_info: ???
                   )

case class SuperpixelFeatures(percentiles: IndexedSeq[BoundaryStrength], size: Int, mean: BoundaryStrength) {
  def toFeatureVector: Vector[FeatureUnit] =
    percentiles.map(_.toDouble).toVector :+ size.toDouble :+ mean.toDouble
}


/**
 *
 * @param pixelNeighbors pixel pairs across SP boundaries.
 *                      each pixel belongs to one of a pair of adjacent SPs
 * @param superpixelNeighbors adjacent superpixel pairs
 * @param edgeletId mapping: pixel pair boundaryPairs(i) corresponds to
 *                  superpixel pair edgeletSP(edgeletId(i))
 *                  Arya feels like this shouldn't exist.
 */

case class SPNeighborData(pixelNeighbors: Seq[BoundaryPair], // 47k
                          superpixelNeighbors: IndexedSeq[Edgelet], // 6k
                          edgeletId: Seq[EdgeletIdx] // 47k (feels like this shouldn't exist)
                           )

case class NeighborData(edgelets: Map[Edgelet,Set[BoundaryPair]])
case class NeighborDataIntensity(edgelets: Map[Edgelet,Map[BoundaryPair,BoundaryStrength]])
object NeighborData {
  import scalaz.syntax.foldable._
  import scalaz.std.set._
  import scalaz.std.map._

  def from1(d: SPNeighborData): NeighborData = {
    import scalaz.std.iterable.iterableSubtypeFoldable
    NeighborData(
      (d.pixelNeighbors zip d.edgeletId) map {
        case (bp, EdgeletIdx(i)) =>
          Map[Edgelet,Set[BoundaryPair]](d.superpixelNeighbors(i) -> Set(bp))
      } suml
    )
  }
}

trait types {
//  sealed trait Segment
//  sealed trait Pixel1D
  //    sealed trait Pixel2D
//  sealed trait SP
//  sealed trait Edgelet

  /** 0 = no unique region; 1 = region 1, ... */
  type LabelMatrix = DenseMatrix[SuperpixelIdx]
  type Segmentation = LabelMatrix
  type BoundaryStrength = Short


  type Timed[A] = State[Timings,A]
}
