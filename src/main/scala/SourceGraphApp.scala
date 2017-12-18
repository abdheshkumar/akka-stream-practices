import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, SourceShape}
import akka.stream.scaladsl.{GraphDSL, Sink, Source, Zip}

import scala.concurrent.Future

/**
  * Created by abdhesh on 30/07/17.
  */
object SourceGraphApp extends App {
  implicit val system = ActorSystem("test")
  implicit val materializer = ActorMaterializer()
  implicit val ex = system.dispatcher
  val pairs = Source.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    // prepare graph elements
    val zip = b.add(Zip[Int, Int]())

    def ints = Source.fromIterator(() => Iterator.from(1)).take(4)

    // connect the graph
    ints.filter(_ % 2 != 0) ~> zip.in0
    ints.filter(_ % 2 == 0) ~> zip.in1

    // expose port
    SourceShape(zip.out)
  })

  val firstPair = pairs.runWith(Sink.seq)
  firstPair.foreach(println)
}
