package com.abtechsoft

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

trait Start extends App {
  implicit val system = ActorSystem("akka-stream-examples")
  implicit val actorMaterializer = ActorMaterializer()
  implicit val ec =  system.dispatcher

  def spin(value: Int): Int = {
    val start = System.currentTimeMillis()
    while ((System.currentTimeMillis() - start) < 10) {}
    value
  }
}

object AkkStream1 extends Start {


  Source(1 to 1000)
    .map(spin)
    .map(spin)
    .runWith(Sink.ignore)
}
