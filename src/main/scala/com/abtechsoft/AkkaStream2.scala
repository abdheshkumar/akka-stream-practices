package com.abtechsoft

import akka.stream.scaladsl.{Sink, Source}

object AkkaStream2 extends Start {
  //async means that each map stage will be executed in a separate actor, with asynchronous
  // message-passing used to communicate between the actors, across the asynchronous boundary.

  Source(1 to 1000)
    .map(spin)
    .async
    .map(spin)
    .runWith(Sink.ignore)
}
