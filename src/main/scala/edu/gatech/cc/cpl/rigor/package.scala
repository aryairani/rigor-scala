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

  def rigor_obj_segments(imgFilePath: String, settings: RigorParam*) = {
    val image: Image = ??? //
    val t = segmenter.precompute_im_data(???, image)

    // todo: compute_segments

    // masks = seg_obj.cut_segs

    // clear_data(seg_obj)

    // write result to disk (seg_objs, masks)
  }


}
