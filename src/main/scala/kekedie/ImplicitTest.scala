package kekedie

object MMonoid {
  def mzero: Int = 0
  def mappend(a: Int, b: Int): Int = a + b
}

object ImplicitTest extends App {

  def sum[A](list: List[A]): A = ???
  def sum(list: List[Int]): Int = {
    list.foldLeft(MMonoid.mzero)(MMonoid.mappend)
  }

  println(sum(List(1,2,3,4,5)))

}

