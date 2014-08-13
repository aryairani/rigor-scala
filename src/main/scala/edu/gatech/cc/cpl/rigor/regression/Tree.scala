package edu.gatech.cc.cpl.rigor.regression

/**
 * Created by arya on 8/9/14.
 */

/** new type for Index into feature vector */
case class FeatureIndex(i: Int) extends AnyVal

sealed abstract trait Tree {
  def eval(features: IndexedSeq[FeatureUnit]): FeatureUnit
}

case class Branch(f: FeatureIndex,
                  lowBoundary: FeatureUnit, lowResult: Tree,
                  highBoundary: FeatureUnit, highResult: Tree,
                  defaultResult: FeatureUnit) extends Tree
{
  def eval(features: IndexedSeq[FeatureUnit]): FeatureUnit =
    if (features(f.i) < lowBoundary) lowResult.eval(features)
    else if (features(f.i) >= highBoundary) highResult.eval(features)
    else defaultResult
}

case class Leaf(value: FeatureUnit) extends Tree {
  def eval(features: IndexedSeq[FeatureUnit]): FeatureUnit = value
}
