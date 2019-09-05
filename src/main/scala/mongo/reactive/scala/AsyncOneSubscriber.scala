package mongo.reactive.scala

import org.reactivestreams.{ Subscriber, Subscription }

/**
 * @author Eugene Kiyski
 */
class AsyncOneSubscriber[A](val cb: Either[Throwable, A] => Unit) extends Subscriber[A]  {

  @volatile var subscription: Subscription = _
  @volatile var a: A = _

  def onSubscribe(s: Subscription): Unit = {
    subscription = s
    subscription.request(1)
  }

  def onNext(t: A): Unit =
    if( a == null) {
      a = t
      subscription.request(1)
    } else
      cb(Left(new RuntimeException("AsyncOneSubscriber got second value")))

  def onError(t: Throwable): Unit = cb(Left(t))

  def onComplete(): Unit =
    if(a != null)
      cb(Right(a))
  else
      cb(Left(new RuntimeException("AsyncOneSubscriber value wasn't got")))
}
