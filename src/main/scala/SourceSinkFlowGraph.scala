import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape, UniformFanInShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, ZipWith}

import scala.concurrent.{Await, Future}

/**
  * Created by abdhesh on 30/07/17.
  */
object SourceSinkFlowGraph extends App {
  implicit val system = ActorSystem("test")
  implicit val materializer = ActorMaterializer()
  implicit val ex = system.dispatcher
  val topHeadSink = Sink.head[Int]
  val bottomHeadSink = Sink.head[Int]
  val sharedDoubler = Flow[Int].map(_ * 2)

  RunnableGraph.fromGraph(GraphDSL.create(topHeadSink, bottomHeadSink, Source.single(1), sharedDoubler)((_, _, _, _)) { implicit builder =>
    (topHS, bottomHS, source, flow) =>
      import GraphDSL.Implicits._
      val broadcast = builder.add(Broadcast[Int](2))
      source ~> broadcast.in

      broadcast.out(0) ~> flow ~> topHS.in
      broadcast.out(1) ~> flow ~> bottomHS.in
      ClosedShape
  }).run()


  val pickMaxOfThree = GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val zip1 = b.add(ZipWith[Int, Int, Int](math.max _))
    val zip2 = b.add(ZipWith[Int, Int, Int](math.max _))
    zip1.out ~> zip2.in0

    UniformFanInShape(zip2.out, zip1.in0, zip1.in1, zip2.in1)
  }

  val resultSink = Sink.head[Int]

  val g = RunnableGraph.fromGraph(GraphDSL.create(resultSink) { implicit b =>
    sink =>
      import GraphDSL.Implicits._

      // importing the partial graph will return its shape (inlets & outlets)
      val pm3 = b.add(pickMaxOfThree)

      Source.single(1) ~> pm3.in(0)
      Source.single(2) ~> pm3.in(1)
      Source.single(3) ~> pm3.in(2)
      pm3.out ~> sink.in
      ClosedShape
  })

  val max: Future[Int] = g.run()
  max.onComplete(println)
}
