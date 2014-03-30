package kekedie

trait GenericCategory[->>[_, _]] {
  def id[A]: A ->> A
  def compose[A,B,C](g: B ->> C, f: A ->> B): A ->> C
}

object Category extends GenericCategory[Function] {
  def id[A]: A => A = a => a
  def compose[A,B,C](g: B => C, f: A => B): A => C = g compose f
}
