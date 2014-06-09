package scala.reactive
package container



import scala.collection._
import scala.annotation.implicitNotFound



trait ReactMap[@spec(Int, Long, Double) K, V <: AnyRef] extends ReactContainer[(K, V)] {

  def apply(k: K): V

  def entries: PairContainer[K, V]

  def react: ReactMap.Lifted[K, V]

}


object ReactMap {

  def apply[@spec(Int, Long, Double) K, V >: Null <: AnyRef](implicit can: ReactHashMap.Can[K, V]) = new ReactHashMap[K, V]

  implicit def factory[@spec(Int, Long, Double) K, V >: Null <: AnyRef] = new ReactBuilder.Factory[(K, V), ReactMap[K, V]] {
    def apply() = ReactHashMap[K, V]
  }

  implicit def pairFactory[@spec(Int, Long, Double) K, V >: Null <: AnyRef] = new PairBuilder.Factory[K, V, ReactMap[K, V]] {
    def apply() = ReactHashMap[K, V]
  }

  trait Lifted[@spec(Int, Long, Double) K, V <: AnyRef] extends ReactContainer.Lifted[(K, V)] {
    val container: ReactMap[K, V]
    def apply(key: K): Reactive[V]
  }

}