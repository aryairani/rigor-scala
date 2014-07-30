package edu.gatech.cc.cpl.rigor

import java.io.File

import scalaz._
import util._

/**
 * Created by arya on 7/29/14.
 */
package object segmenter {
  /** milliseconds */
  type Time = Long
  
  import Timings._
  
  def segmenter_init(seg_obj: ???) {
    // Initialize some global data structures (timers, num_segments)
    // Set up paths for external code
    // Preload data for external libraries (SketchTokens, StructEdges)
      // override StructEdges settings: multiscale = 1, nms = 1
    // If running hochbaum maxflow, initialize threadpool
    // Start total_seg_time, total_init_time timers
  }

  def precompute_im_data(seg_obj: ???) {
    // Decide filename for saving boundaries data, according to boundaries method name

    val tSt: State[Timings,Unit] = setTime(im_pairwise_time) { // t_bndry
      compute_boundaries(outputFile = ???, orig_I = ???, options = ???, preload_data = ???)
    }

    // if debug mode, write thin, fat images to disk  (see diagnostic_methods # print_boundaries)
  }

  def compute_boundaries(outputFile: File, orig_I: Image, options: ???, preload_data: ???) {
    // Choose bndry_func implementation by string name from options
    // Restart boundary timer :P
    // val (bndry_thin, bndry_fat, bndry_extra_info) = bndry_func(orig_I)
    // Stop boundary timer
    // Cache 3 outputs + timer to output file
    // return only bndry_thin, bndry_fat apparently
  }

  import scalaz.syntax.id._

  def compute_superpixels(orig_I: Image, bndry_data: (Image, ???), segm_params: ???) {
    val (bndry_fat, _) = bndry_data
    // start t_sp timer

    val tSt: State[Timings,(Segmentation, Seq[SuperPixel])] = setTime(superpixels_compute_time) { // t_sp
      val w_seg = matlab.watershed(bndry_fat)

      /* fill in the boundary pixels with the neighboring segment with the closest RGB color */
      val sp_seg = fillInSegmentation(orig_I, w_seg, matlab.watershedNoRegion, Connectivity.FourWay)

      // replace remaining noRegion labels with the most frequently occurring (mode) superpixel in the neighborhood
      val updateZeros: Segmentation => Segmentation = ???

      val fullyLabeled_seg = sp_seg |> updateZeros //|> toDouble /*?*/

      def mapFromLabels(seg: Segmentation): Map[Label, Vector[Coord]] = ???

      def superPixels = mapFromLabels(fullyLabeled_seg)

      def superpixelSize(l: Label) = mapFromLabels(fullyLabeled_seg)(l).size

      import scalaz.std.anyVal._
      import scalaz.std.vector._

      val spInfo = superPixels.values.par.map(spCoords => {
        val mean = colorMean(orig_I, spCoords)
        SuperPixel(
          orig_I, // todo do we need this?
          spCoords.size, // sp_seg_szs(i)
          integralMean(spCoords),                 // sp_centroids(i)   -|
          mean,                                    // sp_mean_color(i) -|- fuse these operations?
          colorStdDev(orig_I, mean, spCoords)
        )
      }).toSeq.seq

      //todo do we really need spseg?
      (sp_seg, spInfo)
    }
  }

  case class SuperPixel(image: Image, area: Int, centroid: Coord, colorMean: Float3, colorStdDev: Float3)

  /** 0 = no unique region; 1 = region 1, ... */
  type Label = Int
  type LabelMatrix = Array[Array[Label]]
  type Segmentation = LabelMatrix
//  case class Segmentation(image: LabelMatrix, labels: Seq[Label])


  sealed trait Connectivity
  object Connectivity {
    case object EightWay extends Connectivity
    case object FourWay extends Connectivity
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
  def fillInSegmentation(
                          img: Image,
                          watershed: Segmentation,
                          noRegion: Label = matlab.watershedNoRegion,
                          connectivity: Connectivity = Connectivity.EightWay
                          ): Segmentation =
    ???

  import scalaz.syntax.functor._
  import scalaz.syntax.foldable._
//  def region_centroids_mex[A: Monoid, F[_]:Functor, G[_]:Foldable](seg: F[G[A]]): F[A] = seg.map(_.suml)



}

