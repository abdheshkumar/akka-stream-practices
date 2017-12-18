package com.abtechsoft

import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object AkkaStream3 extends Start {
  //it will use eight-core of the machine
  Source(1 to 1000)
    .mapAsync(4)(x => Future(spin(x)))
    .mapAsync(4)(x => Future(spin(x)))
    .runWith(Sink.ignore)
}
