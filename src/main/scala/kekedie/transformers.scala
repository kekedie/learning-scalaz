package kekedie

import scalaz._
import scalaz.Scalaz._

object transformers extends App {
  val a: OptionT[List, Int] = OptionT[List, Int](List(1.some))
  println(a.map(i => i * 10).run)
  println(OptionT[List, Int](List(none)).map(i => i * 10).run)
  println((for {
    a <- Writer(List("log1"), 11)
    b <- Writer(List("log2"), 22)
  } yield a + b).run)
  val b = WriterT((List("") -> 11).point[Option])
}
