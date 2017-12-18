import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Merge, Sink, Source}

import scala.concurrent.Future

/**
  * Created by abdhesh on 30/07/17.
  */
object MergeSources extends App {
  implicit val system = ActorSystem("test")
  implicit val materializer = ActorMaterializer()
  implicit val ex = system.dispatcher
  val sourceOne = Source(List(1))
  val sourceTwo = Source(List(2))
  val merged = Source.combine(sourceOne, sourceTwo)(Merge(_))

  val mergedResult: Future[Int] = merged.runWith(Sink.fold(0)(_ + _))
}
