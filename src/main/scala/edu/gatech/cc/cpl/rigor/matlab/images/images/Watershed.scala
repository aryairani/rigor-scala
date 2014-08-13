package edu.gatech.cc.cpl.rigor.matlab.images.images

import edu.gatech.cc.cpl.rigor._
import edu.gatech.cc.cpl.rigor.segmenter._

/**
 * Created by arya on 7/29/14.
 */
trait Watershed {
  /** Watershed transform (MATLAB images toolbox)
    *
    * default connectivity: ones(repmat(3,1,ndims(A)))
    * */
  def watershed(image: BoundaryImage): Segmentation = ???
  val watershedNoRegion = SuperpixelIdx(0)

}
