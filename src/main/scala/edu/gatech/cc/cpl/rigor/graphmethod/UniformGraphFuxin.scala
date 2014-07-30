package edu.gatech.cc.cpl.rigor.graphmethod

/**
 * Created by arya on 7/1/14.
 */
class UniformGraphFuxin(
                         submethods: Seq[UniformGraphFuxin.Submethod],
                         graphSeedFrameWeight: Int,
                         pairwise: PairwiseWeights
                         ) extends GraphMethod_ {
  type Submethod = UniformGraph.Submethod
}

object UniformGraphFuxin {
  trait Submethod
  case class Internal(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  case class External(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  case class External2(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  //case object Subframe extends Submethod
}