package com.abtechsoft.customstage

import akka.NotUsed
import akka.stream.scaladsl.Source
import akka.stream.{Graph, SourceShape}
import com.abtechsoft.Start

import scala.concurrent.Future

object SourceMain extends Start {
  /*// A GraphStage is a proper Graph, just like what GraphDSL.create would return
  val sourceGraph: Graph[SourceShape[Int], NotUsed] = new NumbersSource

  // Create a Source from the Graph to access the DSL
  val mySource: Source[Int, NotUsed] = Source.fromGraph(sourceGraph)

  // Returns 55
  val result1: Future[Int] = mySource.take(10).runFold(0)(_ + _)

  // The source is reusable. This returns 5050
  val result2: Future[Int] = mySource.take(100).runFold(0)(_ + _)
  result2.foreach(f => println("::::::::::::::" + f))*/


  /*val resultFuture: NotUsed = Source(1 to 5)
    .via(new Filter(_ % 2 == 0))
    .via(new Duplicator())
    .via(new MapStage(_ / 2))
    .runWith(new StdoutSink)*/

  Source(List(1, 2, 3, 0, 5))
    .runWith(new StdoutSink)
}
