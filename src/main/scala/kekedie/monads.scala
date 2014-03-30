package kekedie

import scalaz.Show
import scalaz.syntax.{ToMonoidOps, ToShowOps}

object monads extends App with ToShowOps with ToMonoidOps {

  case class Name(name: String)
  case object Name extends NameInstances

  trait NameInstances {
    implicit def nameShow: Show[Name] = Show.showA
    implicit def nameMonoid: scalaz.Monoid[Name] = new scalaz.Monoid[Name] {
      def zero: Name = Name("")
      def append(f1: Name, f2: => Name): Name = Name(f1.name + f2.name)
    }
  }

  println(Name("name1").shows)
  println(Name("name1") |+| Name("name2"))

}
