package edu.gatech.cc.cpl.rigor.graphmethod

/**
 * Created by arya on 7/1/14.
 */
class ColorGraphFuxin(
                       submethods: Seq[ColorGraphFuxin.Submethod],
                       graphSeedFrameWeight: Int,
                       graph_unary_exp_scale: Double,
                       pairwise: PairwiseWeights
                       ) extends GraphMethod_ {
  type Submethod = ColorGraphFuxin.Submethod
}

object ColorGraphFuxin {
  trait Submethod
  case class Internal(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  case class External(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  case class External2(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  //case object Subframe extends Submethod
}