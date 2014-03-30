package kekedie

import org.specs2.mutable.Specification
import org.specs2._
import org.scalacheck._

class CategorySpec extends Specification with ScalaCheck {

  import Category._

  "A Category" should {

    val f = (i: Int) => i.toString
    val g = (s: String) => s.length
    val h = (i: Int) => i * i

    "satisfy associativity" in {
      Prop.forAll { (i: Int) =>
        compose(h, compose(g, f))(i) == compose(compose(h, g), f)(i)
      }
    }

    "satisfy identity" in {
      Prop.forAll { (i: Int) =>
        compose(f, id[Int])(i) == compose(id[String], f)(i)
      }
    }

  }

}
