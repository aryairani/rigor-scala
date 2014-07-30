package edu.gatech.cc.cpl

/**
 * Created by arya on 6/30/14.
 */
package object rigor {
  type ??? = Nothing

  type Masks = ???
  type SegObj = ???
  type Time = ???
  type Image = { val pixelByRowCol: Coord => Color }

  type Color = Float3
  type Float3 = (Double,Double,Double)

  type Coord = (Int,Int)

  type SuperpixelData = ???
  type PixelSets = ???
  type SeedSet = ???
  type SeedSets = ??? // (was this supposed to be the same?)

//  def rigor_obj_segments(imgFilePath: String, settings: RigorParam*) = ???


}
