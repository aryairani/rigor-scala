package edu.gatech.cc.cpl.rigor.graphmethod

import edu.gatech.cc.cpl.rigor.superpixelmethods.SuperpixelMethod
import edu.gatech.cc.cpl.rigor.{SeedSets, SegObj}

/**
 * Created by arya on 7/1/14.
 */
trait GraphSeedGenMethod {
  val graph_seed_nums: (Int,Int)
  val graph_seed_region_size: (Int,Int)
  def apply(s: SegObj): SeedSets = ???
}
case class sp_img_grid(graph_seed_nums: (Int,Int),
                       graph_seed_region_size: (Int,Int)
                        ) extends GraphSeedGenMethod
//case object sp_all extends GraphSeedGenMethod
case class sp_clr_seeds(graph_seed_nums: (Int,Int),
                        graph_seed_region_size: (Int,Int),
                        superpixel_method: SuperpixelMethod
                         ) extends GraphSeedGenMethod
//case object sp_seed_sampling extends GraphSeedGenMethod
//case object gen_user_seeds extends GraphSeedGenMethod