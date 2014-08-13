package edu.gatech.cc.cpl

import java.io.File

import breeze.linalg.{DenseVector, DenseMatrix}
import edu.gatech.cc.cpl.rigor.boundaryfuncs.{StructEdges, BoundaryData}
import edu.gatech.cc.cpl.rigor.regression.FeatureUnit

/**
 * Created by arya on 6/30/14.
 */
package object rigor extends types {

  def rigor_obj_segments(image: RGBImage, settings: Param*) = {

    val t = segmenter.precomputeImageData(image, new StructEdges())(???,???)

    // todo: compute_segments

    // masks = seg_obj.cut_segs

    // clear_data(seg_obj)

    // write result to disk (seg_objs, masks)
  }


}
