package mongo.reactive.scala

import org.reactivestreams.{ Subscriber, Subscription }

/**
 * @author Eugene Kiyski
 */
class AsyncFirstSubscriber[A](val cb: Either[Throwable, A] => Unit) extends Subscriber[A]  {

  @volatile var subscription: Subscription = _

  def onSubscribe(s: Subscription): Unit = {
    subscription = s
    subscription.request(1)
  }

  def onNext(t: A): Unit = {
    subscription.cancel()
    cb(Right(t))
  }

  def onError(t: Throwable): Unit = cb(Left(t))

  def onComplete(): Unit = ()
}
