package net.arya.sizedBreeze

import breeze.linalg.support.LiteralRow
import breeze.linalg.{DenseVector, DenseMatrix}
import breeze.math.Semiring
import breeze.storage.DefaultArrayValue
import shapeless.{Sized, Nat}
import shapeless.ops.nat.ToInt

import scala.reflect.ClassTag

/**
 * Created by arya on 7/29/14.
 */
class SzDenseMatrix[T,Rows<:Nat,Cols<:Nat] private(m: DenseMatrix[T])
object SzDenseMatrix {
  def ones[
  V : ClassTag : DefaultArrayValue,
  Rows <: Nat : ToInt,
  Cols <: Nat : ToInt
  ] = new SzDenseMatrix[V,Rows,Cols](DenseMatrix.zeros[V](ToInt[Rows].apply, ToInt[Cols].apply))

  def ones[
  V : ClassTag : DefaultArrayValue : Semiring,
  Rows <: Nat : ToInt,
  Cols <: Nat : ToInt
  ] = new SzDenseMatrix[V,Rows,Cols](DenseMatrix.ones[V](ToInt[Rows].apply, ToInt[Cols].apply))

  def eye[
  V : ClassTag : DefaultArrayValue : Semiring,
  N <: Nat : ToInt
  ] = new SzDenseMatrix[V,N,N](DenseMatrix.eye[V](ToInt[N].apply))

//  def diag[
//  V : ClassTag : DefaultArrayValue : Semiring,
//  N <: Nat : ToInt
//  ] = new SzDenseMatrix[V,N,N](breeze.linalg.diag(DenseVector(1, 2, 3, 4)))

//  DenseMatrix((1,2),(3,4))
//  def apply[R,V](rows: R*)(implicit rl : LiteralRow[R,V], man : ClassTag[V], df: DefaultArrayValue[V]) =

}

class SzDenseVector[T,Len<:Nat] private(v: DenseVector[T])
object SzDenseVector {
  def zeros[T:ClassTag:DefaultArrayValue,Len<:Nat:ToInt] =
    new SzDenseVector[T,Len](DenseVector.zeros[T](ToInt[Len].apply))

  def ones[T:ClassTag:Semiring,Len<:Nat:ToInt] =
    new SzDenseVector[T,Len](DenseVector.ones[T](ToInt[Len].apply))

  def fill[T:ClassTag:Semiring,Len<:Nat:ToInt](v: T) =
    new SzDenseVector[T,Len](DenseVector.fill[T](ToInt[Len].apply, v))
}
