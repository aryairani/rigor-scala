package edu.gatech.cc.cpl.rigor.boundaryfuncs

import edu.gatech.cc.cpl.rigor._
import edu.gatech.cc.cpl.rigor.regression.RegressionParams

/**
 * Created by arya on 8/9/14.
 */

case class StructEdges(stride: ??? = ???,
                       sharpen: ??? = ???,
                       multiscale: Boolean = true,
                       nTreesEval: Int = 4,
                       nms: Boolean = false) extends BoundaryFunc[StructEdgesInfo] {
  def apply(img: RGBImage) = { // edgesDetect(I, st_model)
    StructEdgesInfo(???, ???, ???)
  }
}

object StructEdges {
  implicit val regressionParams: RegressionParams[StructEdges] = ???
//    RegressionParams.load[StructEdges](pair_trees_high_StructEdges.mat)
}

case class StructEdgesInfo(thin: BoundaryImage, fat: BoundaryImage, gPb_orient: ???)

object StructEdgesInfo {
  implicit val boundaryData = new BoundaryData[StructEdgesInfo] {
    def thin(d: StructEdgesInfo): BoundaryImage = d.thin
    def fat(d: StructEdgesInfo): BoundaryImage = d.fat
  }
}
