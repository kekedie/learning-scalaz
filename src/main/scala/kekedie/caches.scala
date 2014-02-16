package kekedie

import scalaz._
import scalaz.Scalaz._

object caches {

  case class Timestamped[A](value: A, timestamp: Long)

  case class Cache[K, V](stats: Map[K, Timestamped[V]]) {
    def get(key: K) = stats get key
    def update(key: K, value: Timestamped[V]): Cache[K, V] = Cache(stats + (key -> value))
  }

  trait CacheLoader[A] {
    def load: A
  }

  trait Caching {
    type Key
    type Value

    implicit val cacheMonoid = new scalaz.Monoid[Cache[Key, Value]] {
      override def zero = Cache(Map.empty[Key, Timestamped[Value]])
      override def append(f1: Cache[Key, Value], f2: => Cache[Key, Value]) = Cache(f1.stats ++ f2.stats)
    }

    def ttl: Long

    def getCacheState(id: Key)(implicit loader: CacheLoader[Value]) = {
      for {
        checkedState <- checkCacheState(id)
        state <- checkedState.cata(State.state[Cache[Key, Value], Value], refresh(id))
      } yield state
    }

    private def checkCacheState(id: Key): State[Cache[Key, Value], Option[Value]] = for {
      state <- State.gets { c: Cache[Key, Value] =>
        c.get(id).collect {
          case Timestamped(value, ts) if !stale(ts) => value
        }
      }
    } yield state

    private def stale(ts: Long): Boolean =
      System.currentTimeMillis - ts > ttl

    private def refresh(id: Key)(implicit loader: CacheLoader[Value]): State[Cache[Key, Value], Value] = for {
      state <- State.state(loader.load)
      t = Timestamped(state, System.currentTimeMillis)
      _ <- State.modify[Cache[Key, Value]] { _.update(id, t) }
    } yield state
  }

}

object CacheState extends App {
  import caches._

  class Entity(val id: Int)

  object EntityCaching extends Caching {
    type Key = Int
    type Value = Entity

    @volatile private var cacheState: Cache[Int, Entity] = cacheMonoid.zero

    def ttl = 3 * 1000L

    def get(id: Int): Entity = {
      implicit val loader = new CacheLoader[Entity] {
        override def load: Entity = new Entity(id)
      }
      synchronized {
        val state = getCacheState(id).run(cacheState)
        println(s"old state: $cacheState, new state: $state")
        cacheState = state._1
        state._2
      }
    }
  }

  val entity1: Entity = EntityCaching.get(100)
  val entity2: Entity = EntityCaching.get(100)
  assert(entity1 == entity2)

  Thread.sleep(3500L)

  val entity3: Entity = EntityCaching.get(100)
  assert(entity2 != entity3)
}
