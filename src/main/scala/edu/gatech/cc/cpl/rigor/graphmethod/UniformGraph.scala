package edu.gatech.cc.cpl.rigor.graphmethod

/**
 * Created by arya on 7/1/14.
 */
object UniformGraph {
  sealed trait Submethod
  case class Internal(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  case class External(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
}

case class UniformGraph(
                         submethods: Seq[UniformGraph.Submethod],
                         graphSeedFrameWeight: Int,
                         pairwise: PairwiseWeights) extends GraphMethod_ {
  type Submethod = UniformGraph.Submethod
}