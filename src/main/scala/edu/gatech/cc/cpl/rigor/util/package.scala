package edu.gatech.cc.cpl.rigor

import scalaz.{Monoid, Functor, Foldable}
import scalaz.syntax.functor._
import scalaz.syntax.foldable._
import scalaz.std.tuple._
import net.arya.instances.scalazInstances.doubleMonoid

/**
 * Created by arya on 7/30/14.
 */
package object util {

  implicit class DoubleOps(x: Double) {
    def clamp(min: Double, max: Double) = Math.max(min, Math.min(max, x))
  }
  implicit class IntOps(x: Int) {
    def clamp(min: Int, max: Int) = Math.max(min, Math.min(max,x))
  }

  def float3Mean[F[_]:Foldable](values: F[Float3]): Float3 = {
    val (sum1, sum2, sum3) = values.suml
    val len = values.length
    (sum1 / len, sum2 / len, sum3 / len)
  }

  def float3StdDev[F[_]:Functor:Foldable](mean: Float3, values: F[Float3]): Float3 = {
    import math.{sqrt => s}
    val (x, y, z) = floatVariance(mean, values)
    (s(x), s(y), s(z))
  }

  def floatVariance[F[_]:Functor:Foldable](mean: Float3, values: F[Float3]): Float3 = {
    val len = values.length
    val (sum1, sum2, sum3) = values.map(v => (
      math.pow(v._1 - mean._1, 2),
      math.pow(v._2 - mean._2, 2),
      math.pow(v._3 - mean._3, 2)
      )
    ).suml
    (sum1 / len, sum2 / len, sum3 / len)
  }

  def fractionalMean[A:Monoid,F[_]:Foldable](values: F[(A,A,A)])(implicit N:Fractional[A]) = {
    import N.mkNumericOps
    val (sum1, sum2, sum3) = values.suml
    val len = N.fromInt(values.length)
    (sum1 / len, sum2 / len, sum3 / len)
  }

  def integralMean[A:Monoid,F[_]:Foldable](values: F[(A,A)])(implicit N:Integral[A]) = {
    import N.mkNumericOps
    val (sumX, sumY) = values.suml
    val len = N.fromInt(values.length)
    (sumX / len, sumY / len)
  }

  def integralMean[A:Monoid,F[_]:Foldable](values: F[A])(implicit N:Integral[A]) = {
    import N.mkNumericOps

    val (sum) = values.suml
    val len = N.fromInt(values.length)
    (sum / len)
  }

  implicit class ImageSyntax(img: RGBImage) {
    def pixelByRowCol(c: Coord) = img.valueAt(c._1,c._2)
  }

  // compute the mean color for each superpixel
  def colorMean[F[_]:Functor:Foldable](img: RGBImage, coords: F[Coord]): Float3 =
    float3Mean(coords.map(img.pixelByRowCol))

  // compute the stddev of rgb for each superpixel
  def colorStdDev[F[_]:Functor:Foldable](img: RGBImage, mean: Float3, coords: F[Coord]): Float3 =
    float3StdDev(mean, coords.map(img.pixelByRowCol))
}
