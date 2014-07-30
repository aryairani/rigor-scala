package net.arya.instances

import scala.collection.parallel.ParSeq
import scala.collection.parallel.ParIterable
import scala.collection.parallel.immutable.ParSet
import scalaz.{Functor, Foldable, Monoid}

object scalazInstances {

  implicit val parIterableInstance = new Functor[ParIterable] with Foldable[ParIterable] {
    def map[A, B](fa: ParIterable[A])(f: (A) => B): ParIterable[B] = fa.map(f)

    /** Map each element of the structure to a [[scalaz.Monoid]], and combine the results. */
    def foldMap[A, B](fa: ParIterable[A])(f: (A) => B)(implicit F: Monoid[B]): B =
      fa.map(f).fold(F.zero)(F.append(_,_))

    def foldRight[A, B](fa: ParIterable[A], z: => B)(f: (A, => B) => B): B =
      fa.foldRight(z)(f(_,_))

  }

  implicit val parSetFoldable = new Foldable[ParSet] {
    def foldMap[A, B](fa: ParSet[A])(f: (A) => B)(implicit F: Monoid[B]): B =
      fa.map(f).fold(F.zero)(F.append(_,_))

    def foldRight[A, B](fa: ParSet[A], z: => B)(f: (A, => B) => B): B =
      fa.foldRight(z)(f(_,_))
  }
}
