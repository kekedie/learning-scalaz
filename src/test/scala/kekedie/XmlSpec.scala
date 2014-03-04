package kekedie

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.matcher.{ResultMatchers, XmlMatchers}
import scala.xml._

class XmlSpec extends SpecificationWithJUnit with XmlMatchers with ResultMatchers {

  implicit def pimpedNode(n: Node): PimpedNode = PimpedNode(n)
  case class PimpedNode(underlying: Node) {
    def trim: Node = Utility.trim(underlying)
  }

  val banner =
    <banner>
      ad
    </banner>

  "node seq" should {
    "equal" in {
      val data = <css/> ++ <js/> ++ banner.trim
      data must beEqualToIgnoringSpace(<css/> ++ <js/> ++ <banner>ad</banner>)
    }
  }

  "regex" should {
    "equal" in {
      val xml = """<css/><js/><banner>ad</banner>"""
      val regex = """<css/><js/>(.+)""".r
      val ad = Option(xml).collect { case regex(ad) => ad }.map(XML.loadString)
      xml must beMatching(regex)
      ad must beSome[Elem].which(_ === banner.trim)
    }
  }

}
