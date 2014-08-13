package edu.gatech.cc.cpl.rigor.regression

/**
 * Created by arya on 8/9/14.
 */

case class RegressionParams[B](rankerName: String, treeWeight: Seq[TreeWeight], f0: FeatureUnit)
// scaling stuff unused for now

case class TreeWeight(tree: Tree, ρ: FeatureUnit) {
  def rho = ρ
}
