package kekedie

import java.io._
import resource._
import scala.annotation.tailrec
import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.concurrent.forkjoin.ForkJoinPool
import scalaz.OptionT._
import scalaz.Scalaz._
import scalaz._
import scalaz.contrib.std._

object files extends App {

  implicit val es = ExecutionContext.fromExecutor(new ForkJoinPool(6))

  val chunks = 6
//  val file = new File("/Users/kekedie/Downloads/test.txt")
  val file = new File("/Users/kekedie/Downloads/enwiki-latest-all-titles")

  val resultF = for {
    offsets <- optionT(getOffsets(file, chunks).pure[Future])
    ranges  <- getStartToEnd(offsets).pure[Future].liftM[OptionT]
    counts  <- ranges.traverseU { range =>
      optionT(importKvs(file, range(0), range(1)).pure[Future])
    }
  } yield counts

  println("start")

  val result = Await.result(resultF.run, Duration.Inf)
  result match {
    case Some(l) => println(s"end. cnt: ${l.sum}")
    case None => println("end with none.")
  }

  def importKvs(file: File, start: Long, end: Long): Option[Int] = {
    managed(new RandomAccessFile(file, "r")).map { raf =>
      raf.seek(start)

      @tailrec def go(cnt: Int): Int = {
        val current = raf.getFilePointer
        if (current < end) {
          val line = raf.readLine()
          println(line)
          go(cnt + 1)
        } else cnt
      }

      go(0)
    }.opt
  }

  def getOffsets(file: File, chunks: Int): Option[List[Long]] = {
    managed(new RandomAccessFile(file, "r")).map { raf =>
      val offsets = (1 until chunks).map { i =>
        val pos = i * file.length() / chunks
        raf.seek(pos)

        @tailrec def go: Long = {
          val read = raf.read()
          if (read == '\n' || read == -1) raf.getFilePointer
          else go
        }

        go
      }

      (List(0L) ::: offsets.toList ::: List(file.length)).distinct
    }.opt
  }

  def getStartToEnd(xs: List[Long]): List[List[Long]] = {

    @tailrec def go(l: List[List[Long]], ys: List[Long]): List[List[Long]] = {
      if (ys == Nil) l
      else {
        val (ys1, _) = ys.splitAt(2)
        val (_, zs2) = ys.splitAt(1)
        if (zs2 == Nil) l
        else go(ys1 :: l, zs2)
      }
    }

    go(List(xs.splitAt(2)._1), xs.splitAt(1)._2).reverse
  }

}
