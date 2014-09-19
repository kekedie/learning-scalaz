package fbteam

object ImplicitTest extends App {

  def greeting(prefix: String)(implicit name: String): String = {
    prefix + name
  }

  implicit val name = "oshiro"
  println(greeting("hello"))

  implicit def intToString(i: Int): String = i.toString + "(converted), "

  println(greeting(1))

  case class RichString(s: String) {
    def upper = s.toUpperCase
  }

  implicit def stringToRich(s: String): RichString = RichString(s)

  println("oshiro".upper) // OSHIRO


}
