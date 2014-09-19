package dynalyst

import scalaz._
import scalaz.Scalaz._

//trait Monoid[A] {
//  def mzero: A
//  def mappend(a: A, b: A): A
//}
//
//object Monoid {
//
//  implicit object IntMonoid extends Monoid[Int] {
//    def mzero = 0
//    def mappend(a: Int, b: Int) = a + b
//  }
//
//  implicit object StringMonoid extends Monoid[String] {
//    def mzero = ""
//    def mappend(a: String, b: String) = a + b
//  }
//
//  implicit object FloatMonoid extends Monoid[Float] {
//    def mzero = 0.0f
//    def mappend(a: Float, b: Float) = a + b
//  }
//
//}
//
//trait Foldable[M[_]] {
//  def fold[A](xs: M[A])(implicit m: Monoid[A]): A
//}
//
//object Foldable {
//
//  implicit object ListFoldable extends Foldable[List] {
//    def fold[A](xs: List[A])(implicit m: Monoid[A]) = {
//      xs.foldLeft(m.mzero)(m.mappend)
//    }
//  }
//
//  implicit object OptionFoldable extends Foldable[Option] {
//    def fold[A](xs: Option[A])(implicit m: Monoid[A]) = {
//      xs.foldLeft(m.mzero)(m.mappend)
//    }
//  }
//
//  implicit object ArrayFoldable extends Foldable[Array] {
//    def fold[A](xs: Array[A])(implicit m: Monoid[A]) = {
//      xs.foldLeft(m.mzero)(m.mappend)
//    }
//  }
//
//  implicit object VectorFoldable extends Foldable[Vector] {
//    def fold[A](xs: Vector[A])(implicit m: Monoid[A]) = {
//      xs.foldLeft(m.mzero)(m.mappend)
//    }
//  }
//
//}
//
//trait Sum[A] {
//  val identity: A
//  def plus(other: A)(implicit m: Monoid[A]): A = m.mappend(identity, other)
//  def |+|(other: A)(implicit m: Monoid[A]): A = plus(other)
//}

object AppTest extends App {

//  def sum(xs: List[Int]): Int = {
//    xs.foldLeft(0)(_ + _) -> 1.
//    xs.foldLeft(IntMonoid.mzero)(IntMonoid.mappend) -> 2.
//  def sum(xs: List[Int])(m: IntMonoid): Int = {
//    xs.foldLeft(m.mzero)(m.mappend) -> 3.
//  def sum(xs: List[Int])(m: Monoid[A]): Int = {
  //    xs.foldLeft(m.mzero)(m.mappend) -> 4.
//  }

  def sum[M[_], A](xs: M[A])(implicit m: Monoid[A], fl: Foldable[M]): A = {
    fl.fold(xs)(m)
  }

//  def sum[M[_], A](xs: M[A])(implicit m: Monoid[A]): A = {
//    ListFoldable.fold(xs, m)
//  }

  println(sum(List(1, 2, 3)))
  println(sum(List("1", "2", "3")))
//  println(sum(List(1.0f, 2.0f, 3.0f)))
  println(sum(Vector(1, 2, 3)))
  println(sum(Option(1)))
//
//  println(sum(Option(1)))

//  implicit def intToSumInt(i: Int): Sum[Int] = new Sum[Int] {
//    val identity: Int = i
//  }
//
//  def plus[A](identity: A, other: A)(implicit m: Monoid[A]): A = m.mappend(identity, other)
//  println(plus(1, 2))
  println(1 |+| 2)
  println(List(1, 2, 3) |+| List(4, 5, 6))

}
