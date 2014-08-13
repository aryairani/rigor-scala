package edu.gatech.cc.cpl.rigor

import edu.gatech.cc.cpl.rigor.boundaryfuncs.BoundaryData
import edu.gatech.cc.cpl.rigor.regression._

/**
 * Created by arya on 8/13/14.
 */
trait types {

  type ??? = Nothing

  type Masks = ???
  type RGBImage = breeze.linalg.DenseMatrix[Color]//{ val pixelByRowCol: Coord => Color } // w * h * 3

  type Color = Float3
  type Float3 = (Double,Double,Double)

  type Coord = (Int,Int)

  type SuperpixelData = ???
  type PixelSets = ???
  type SeedSet = ???
  type SeedSets = ??? // SeedSets = SeedSet?  or SeedSets = List[SeedSets]?

  type BoundaryFunc[B] = RGBImage => B
  type BoundaryImage = breeze.linalg.DenseMatrix[Float]
  type MaskImage = breeze.linalg.DenseMatrix[Boolean]
  type EdgeVals = Vector[FeatureUnit]

  type FrameSet = ???
  type Seeds = ???
}
