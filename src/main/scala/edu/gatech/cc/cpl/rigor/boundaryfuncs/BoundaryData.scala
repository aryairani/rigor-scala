package edu.gatech.cc.cpl.rigor.boundaryfuncs

import breeze.linalg.DenseMatrix
import edu.gatech.cc.cpl.rigor.{BoundaryImage, RGBImage}

/**
 * Created by arya on 7/1/14.
 */

trait BoundaryData[D] {
  /** used in precomputePairwiseData */
  def thin(d: D): BoundaryImage

  /** used in computeSuperpixels */
  def fat(d: D): BoundaryImage
}
