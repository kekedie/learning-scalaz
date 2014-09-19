package fbteam

//import scalaz._
//import scalaz.Scalaz._

trait Monoid[A] {
  def mzero: A
  def mappend(a: A, b: A): A
}

object Monoid {

  implicit object IntMonoid extends Monoid[Int] {
    def mzero = 0
    def mappend(a: Int, b: Int) = a + b
  }

  implicit object StringMonoid extends Monoid[String] {
    def mzero = ""
    def mappend(a: String, b: String) = a + b
  }

}

trait Foldable[F[_]] {
  def fold[A](xs: F[A])(implicit m: Monoid[A]): A
}

object Foldable {

  implicit object ListFoldable extends Foldable[List] {
    def fold[A](xs: List[A])(implicit m: Monoid[A]): A = {
      xs.foldLeft(m.mzero)(m.mappend)
    }
  }

  implicit object VFoldable extends Foldable[Vector] {
    def fold[A](xs: Vector[A])(implicit m: Monoid[A]): A = {
      xs.foldLeft(m.mzero)(m.mappend)
    }
  }

  implicit object OFoldable extends Foldable[Option] {
    def fold[A](xs: Option[A])(implicit m: Monoid[A]): A = {
      xs match {
        case Some(a) => a
        case None => m.mzero
      }
    }
  }

}

trait Plus[A] {
  val identity: A
  def plus(other: A)(implicit m: Monoid[A]): A = m.mappend(identity, other)
  def |+|(other: A)(implicit m: Monoid[A]): A = plus(other)
}

object MonoidTest extends App {

  def sum[F[_], A](xs: F[A])(implicit m: Monoid[A], foldable: Foldable[F]): A = {
    foldable.fold(xs)
  }

  implicit def IntPlus(id: Int) = new Plus[Int] {
    val identity: Int = id
  }

  println(1 |+| 2)

  println(
    sum(List(1, 2, 3))
  )

  println(
    sum(Vector(1, 2, 3))
  )

  println(
    sum(Option(1))
  )

  println(
    sum(None: Option[Int])
  )

  println(
    sum[Option, String](None)
  )

  println(
    sum(Option("hi"))
  )

  println(
    sum(
      List("hello", "oshiro"))
  )

}

