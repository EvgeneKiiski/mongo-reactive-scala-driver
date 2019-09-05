package mongo.reactive.scala

import org.reactivestreams.{ Subscriber, Subscription }

/**
 * @author Eugene Kiyski
 */
final case class PrintSubscriber[A]() extends Subscriber[A]  {

  @volatile var subscription: Subscription = _

  def onSubscribe(s: Subscription): Unit = {
    subscription = s
    subscription.request(1)
  }

  def onNext(t: A): Unit = {
    println(s"onNext $t")
    subscription.request(1)
  }

  def onError(t: Throwable): Unit = println(s"Failed $t")

  def onComplete(): Unit = println("Completed")
}
