package edu.gatech.cc.cpl.rigor
package boundaryfuncs

import edu.gatech.cc.cpl.rigor.???

/**
 * Created by arya on 7/1/14.
 */
case class Boundaries(thin: Image, fat: Image, extra_info: ???)

trait BoundaryFunc {
  def apply(img: Image): Boundaries = ???
}

case object Gb extends BoundaryFunc
case object GPb extends BoundaryFunc
case object Pb extends BoundaryFunc
case object SketchTokens extends BoundaryFunc
case object StructEdges extends BoundaryFunc
