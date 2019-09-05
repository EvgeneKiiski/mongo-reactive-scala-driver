package mongo.reactive

import cats.effect.Async
import org.reactivestreams.Publisher

/**
 * @author Eugene Kiyski
 */
package object scala {

  implicit class SubscriberOps[A](val publisher: Publisher[A]) extends AnyVal {
    def toAsync[F[_]: Async]: F[A] = Async[F].async { cb =>
      publisher.subscribe(new AsyncFirstSubscriber(cb))
    }

    def toAsyncSeq[F[_]: Async]: F[Seq[A]] = Async[F].async { cb =>
      publisher.subscribe(new AsyncSeqSubscriber(cb))
    }
  }

}
