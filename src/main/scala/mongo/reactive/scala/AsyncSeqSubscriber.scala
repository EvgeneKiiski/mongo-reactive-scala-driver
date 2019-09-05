package mongo.reactive.scala

import org.reactivestreams.{Subscriber, Subscription}

import scala.collection.mutable

/**
  * @author Eugene Kiyski
  */
class AsyncSeqSubscriber[A](val cb: Either[Throwable, Seq[A]] => Unit)
    extends Subscriber[A] {

  val builder: mutable.Builder[A, Seq[A]] = Seq.newBuilder[A]
  @volatile var subscription: Subscription = _

  def onSubscribe(s: Subscription): Unit = {
    subscription = s
    subscription.request(1)
  }

  def onNext(t: A): Unit = {
    builder += t
    if (subscription != null)
      subscription.request(1)
    else
      cb(Left(new RuntimeException("AsyncSeqSubscriber not initialized")))
  }

  def onError(t: Throwable): Unit = cb(Left(t))

  def onComplete(): Unit = cb(Right(builder.result()))
}
