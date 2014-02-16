package kekedie

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._
import scalaz.Scalaz._
import scalaz.contrib.std._

object futures {

  sealed trait Sex
  case object Male extends Sex
  case object Female extends Sex

  def name(name: String) = future {
    println(s"name: $name")
    name
  }

  def age(age: Int) = future {
    println(s"age: $age")
    Thread.sleep(1000L)
    age
  }

  def sex(sex: Sex) = future {
    println(s"sex: $sex")
    Thread.sleep(2000L)
    sex
  }

}

object FutureApplicative extends App {

  import futures._

  val ap = (name("han") |@| age(34) |@| sex(Male)) {
    (n, a, s) => s"$n, $a, $s"
  }
  println(Await.result(ap, 10.seconds))
}