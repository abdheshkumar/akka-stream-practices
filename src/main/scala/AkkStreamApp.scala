import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, Merge, RunnableGraph, Sink, Source}

import scala.concurrent.Future

/**
  * Created by abdhesh on 30/07/17.
  */
object AkkStreamApp extends App {
  implicit val system = ActorSystem("test")
  implicit val materializer = ActorMaterializer()
  implicit val ex = system.dispatcher

  val source = Source(1 to 10)
  val sink = Sink.fold[Int, Int](0)(_ + _)
  val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)
  val sum: Future[Int] = runnable.run()

  val a: Source[Int, NotUsed] = Source(List(1, 2, 3))
  val b: Source[String, NotUsed] = Source.fromFuture(Future.successful("Hello Streams!"))
  val out = Sink.head[Int] //Sink.foreach(println)
  //http://doc.akka.io/docs/akka/2.5.3/scala/stream/stream-graphs.html
  val g = RunnableGraph.fromGraph(GraphDSL.create(out) { implicit builder =>
    sink =>
      import GraphDSL.Implicits._

      val in = Source(1 to 10)


      val bcast = builder.add(Broadcast[Int](2))
      val merge = builder.add(Merge[Int](2))

      val f1, f2, f3, f4 = Flow[Int].map(_ + 10)

      in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> sink
      bcast ~> f4 ~> merge

      ClosedShape
  })

  val res = g.run() // here i want to capture output into res.
  println(res)
}
