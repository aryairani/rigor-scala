package edu.gatech.cc.cpl.rigor.matlab.images.colorspaces

import edu.gatech.cc.cpl.rigor.Image

/**
 * Created by arya on 7/29/14.
 */
trait ApplyCForm {

  /**
   * Apply device-independent color space transformation.
   * B = APPLYCFORM(A, C) converts the color values in A to the color space
   * specified in the color transformation structure, C.  The color
   * transformation structure specifies various parameters of the
   * transformation.
   */
//  def applycform(a: Image): Image

  def srgb2lab(a: Image): Image = ???

}
