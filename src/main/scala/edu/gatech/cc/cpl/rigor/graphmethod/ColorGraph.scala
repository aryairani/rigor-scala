package edu.gatech.cc.cpl.rigor.graphmethod

/**
 * Created by arya on 7/1/14.
 */
object ColorGraph {
  trait Submethod
  case class Internal(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  case class External(graph_seed_gen_method: GraphSeedGenMethod) extends Submethod
  //case object Subframe extends Submethod

}

class ColorGraph(submethods: Seq[ColorGraph.Submethod],
                 graphSeedFrameWeight: Double,
                 graph_unary_exp_scale: Double,
                 pairwise: PairwiseWeights
                  ) extends GraphMethod_ {
  type Submethod = ColorGraph.Submethod
}

