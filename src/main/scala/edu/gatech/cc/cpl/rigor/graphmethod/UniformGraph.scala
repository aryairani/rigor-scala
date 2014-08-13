package edu.gatech.cc.cpl.rigor.graphmethod

/**
 * Created by arya on 7/1/14.
 *
 * Modeled after UniformGraphFuxin
 */
class UniformGraph(
                    submethods: Seq[UniformGraph.Submethod],
                    graphSeedFrameWeight: Int,
                    pairwise: PairwiseWeights
                    ) extends GraphMethod {
  type Submethod = UniformGraph.Submethod
}

object UniformGraph {
  trait Submethod
  case class Internal(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  case class External(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  case class External2(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  //case object Subframe extends Submethod
}