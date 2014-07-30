package edu.gatech.cc.cpl.rigor.superpixelmethods

import edu.gatech.cc.cpl.rigor.{SuperpixelData, PixelSets, ???}

/**
 * Created by arya on 7/2/14.
 */
trait SuperpixelMethod {
  // (sp_data, I, any*) => PixelSets
  def apply(sp_data: SuperpixelData, rect_coords: (Int,Int,Int,Int), graph_num_seed_locs: Int): PixelSets = ???
}

case class felzenszwalb_seeds_caller(sigma_k: Double = 0.1, k: Int = 400, min_sz: Int = 100) extends SuperpixelMethod
case object sp_seeds_caller extends SuperpixelMethod