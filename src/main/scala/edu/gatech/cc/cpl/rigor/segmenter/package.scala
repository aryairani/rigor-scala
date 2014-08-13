package edu.gatech.cc.cpl.rigor

import edu.gatech.cc.cpl.rigor.boundaryfuncs.BoundaryData
import edu.gatech.cc.cpl.rigor.regression.{TreeFeatures, FeatureUnit, RegressionParams}

import scalaz._
import util._
import scalaz.std.anyVal._
import scalaz.std.vector._
import scalaz.std.map._
import scalaz.std.tuple._
import scalaz.syntax.id._
import scalaz.syntax.foldable._
import timing.setTime
import edu.gatech.cc.cpl.rigor.segmenter.Timings._


/**
 * Created by arya on 7/29/14.
 */
package object segmenter extends segmenter.types {

  def precomputeImageData[B:RegressionParams](image: RGBImage, boundaryFunc: BoundaryFunc[B])
                                             (implicit boundaryOps: BoundaryData[B]) = {

    val boundaryData = boundaryFunc(image)
    val sp = computeSuperpixels(image, boundaryOps.fat(boundaryData))
    val edgeVals = precomputePairwiseData(sp.seg, boundaryOps.thin(boundaryData))
    // todo: precompute unaryData
  }

  def compute_segments[B](params: SegmParams[B]): Seq[MaskImage] = ???
/* highly busted - arya
{

    // the output will be num_spx long???  why


    // foreach graph method,
      // start timing t_seg_all

    {

      // prepare graphs:
      // compute all the unary values for all seeds for each graph submethod (stored in graph_obj.graph_unaries_all
      // also compute the final binary capacities between superpixel pairs (stored in graph_obj.edge_vals)

      /**
       * graphprocessor handles the parametric min-cut/maxflow (graph-cuts on Ising model) to produce
       * binary segments.  In the constructor, it prepares a data structure for min-cut
       */
      val gp_obj = graphProcessor(graph_obj)

      /** function where GraphProcessor computes min-cut for all the graph submethods */
      val currentSegments = generate_mincut_segments(gp_obj)

      /** here the Segmenter filters the output min-cut segments produced by GraphProcessor */
      currentSegments2 = filter_segments(seg_obj, gp_obj, graph_idx, currentSegments)

      val curr_cut_segs = size(currentSegments2.cutSegs,2)
      seg_obj.num_segs.after_clustering_FINAL += curr_cut_segs

      println(s"FinalNumber of Segments: $curr_cut_segs")

      seg_obj.cutSegs += currentSegments2.cutSegs

      case class MetaInfo(sols_to_unary_mapping: ???,
                          lambdas: ???,
                          mincut_vals: ???,
                          extra_cut_info: ???,
                          seg_mapping_final_to_orig: ???,
                          energies: ???)

      seg_obj.metaCutSegsInfo += currSegs.segsMetaInfo

//    total_computing_segs_time
    }

    /** converting superpixel segments into full image segments */
    ??? convert_masks(???)
  }

*/

  def generate_sp_img_frame(x : ???, frameWidth: Int = 1): FrameSet = ???

  def precompute_seeds(): Seeds  = ???

  /** "Precompute anything related to pairwise potentials."
    * "Trees seem to always help"
    */
  def precomputePairwiseData[B](segmentation: Segmentation,
                                          boundary: BoundaryImage)
                                         (implicit treeParams: RegressionParams[B]): EdgeVals =
  {

    def generateNeighborData(seg: Segmentation): NeighborData = ???

    /** Adjust boundary to coincide exactly with superpixel borders */
    // "shift all bndry values up by the min if any below 0"
    def adjustBoundaries(boundary: BoundaryImage, seg: Segmentation): BoundaryImage = ???

    def evalTree(features: TreeFeatures): FeatureUnit = {
      treeParams.treeWeight.map { c =>
        import treeParams.f0
        import c.{ρ, tree}

        f0 + ρ * tree.eval(features)
      }.sum
    }

    generateSuperpixelFeatures( // features
      computeBoundaryStrengths( // boundaryStrength
        adjustBoundaries(boundary, segmentation),
        generateNeighborData(segmentation) // superpixel boundary info
      )
    ) map {
      /* Sometimes instead of features, we just get a static value back --- `the_rest` */
      case Left(staticTerm) => staticTerm
      /* otherwise run the features through the regression tree */
      case Right(spFeatures) => evalTree(spFeatures.toFeatureVector) max 0
    }

  }

  type StaticTerm = FeatureUnit

  /** Each SP pair has a corresponding set of BP pairs
    * For each SP pair, compute the intensity values for
    *   the 10,20,...,100th percentile for the set of BP pairs
    *
    * Output is percentiles, count, and mean for each SP pair.
    */
  def generateSuperpixelFeatures(neighborData: NeighborDataIntensity): Vector[Either[StaticTerm,SuperpixelFeatures]] = {

    /** return the 10th, 20th, ... 100th percentile boundary strength based on input */
    def gen10Percentiles(values: Vector[BoundaryStrength]): IndexedSeq[BoundaryStrength] = {
      val sorted = values.sorted.toIndexedSeq
      val percentiles = (10 to 100 by 10) map { p =>
        val fractionalIdx = (sorted.length * (p / 100.0))
        val ceilIdx = fractionalIdx.ceil.toInt
        sorted(ceilIdx.clamp(0,sorted.length))
      }
      percentiles
    }

    /** compute the percentiles and a couple other features */
    def gen1(values: Vector[BoundaryStrength]) =
      SuperpixelFeatures(gen10Percentiles(values), values.length, integralMean(values))

    /** some edgelets are `selected`; we generate regression tree features for them.
      * the non-selected edgelets (`the rest`) just have statically assigned weights, I guess? */
    neighborData.edgelets.values.map(boundaryPairs => 
      if (???) // the selection criteria
        Right( gen1(boundaryPairs.values.toVector) )
      else {
        Left(??? : StaticTerm)
      }
    ).toVector
  }

  def computeBoundaryStrengths(image: BoundaryImage, neighborData: NeighborData): NeighborDataIntensity = {

    def pixelAt(i: Int) = {
      val index: (Int, Int) = image.rowColumnFromLinearIndex(i)
      image.valueAt(index._1,index._2)
    }

    def boundaryStrength(bp: BoundaryPair): BoundaryStrength = {
      (Math.abs(pixelAt(bp.p1.i) - pixelAt(bp.p2.i)) * 255).toShort
    }

    NeighborDataIntensity(
      neighborData.edgelets.mapValues(boundaryPairs =>
        boundaryPairs.map(bp => bp -> boundaryStrength(bp)).toMap
      )
    )
  }

  def computeSuperpixels(image: RGBImage,
                          boundaryFat: BoundaryImage): SuperPixelData = {

    /* replace remaining noRegion labels with the most frequently occurring (mode) superpixel in the neighborhood */
    def replaceZerosWithMode(seg: Segmentation): Segmentation = ??? // todo

    def pixelCoordsByLabel(seg: Segmentation): Map[SuperpixelIdx, Vector[Coord]] = {
      import scalaz.std.iterable.iterableSubtypeFoldable
      seg.activeIterator.toIterable.map {
        case (coord, label) => Map(label -> Vector(coord))
      }.suml
    }

    val segmentation: Segmentation =
      boundaryFat |>
        matlab.watershed |>
        (fillInSegmentation(image, _, matlab.watershedNoRegion, Connectivity.FourWay)) |>
        replaceZerosWithMode

    assert(segmentation.forall(_ != matlab.watershedNoRegion)) // none are unlabeled

    /* for each superpixel, find the area, centroid, color mean, and color stddev */
    val spInfo: IndexedSeq[SuperPixel] =
      pixelCoordsByLabel(segmentation).values.par.map {
        coords => {
          val mean = colorMean(image, coords)
          SuperPixel(
            coords.size, // area
            integralMean(coords), // centroid
            mean, // color mean
            colorStdDev(image, mean, coords)
          )
        }
      }.toIndexedSeq

    /* return the segmentation and the superpixel statistics */
    SuperPixelData(segmentation, spInfo)
  }

  /** Fills in the unlabeled pixels of a segmentation 'seg'.  Those pixels
    * that are pixels with a segmentation label `noRegion` do not belong to any
    * valid region.  Fill them in by associating them with the neighboring
    * region with the most similar color (i.e. by Euclidean distance).
    * By default, it is assumed seg contains labels 1:N and that L=0.
    * (As is the case with a watershed segmentation.)
    * 
    * Connectivity is specified by 'conn'.  Default is EightWay, but Connectivity.FourWay can also be
    * used.
    */
  // ToDo (see fill_in_segmentation.m)
  def fillInSegmentation(img: RGBImage,
                         watershed: Segmentation,
                         noRegion: SuperpixelIdx = matlab.watershedNoRegion,
                         connectivity: Connectivity = Connectivity.EightWay
                          ): Segmentation =
    ???

}

