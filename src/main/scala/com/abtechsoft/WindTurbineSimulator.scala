package com.abtechsoft

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.StatusCode
import akka.stream.ActorMaterializer
import com.abtechsoft.WindTurbineSimulator._

object WindTurbineSimulator {
  def props(id: String, endpoint: String)(implicit materializer: ActorMaterializer) =
    Props(classOf[WindTurbineSimulator], id, endpoint, materializer)

  final case object Upgraded
  final case object Connected
  final case object Terminated
  final case object QueryActorPath
  final case class ConnectionFailure(ex: Throwable)
  final case class FailedUpgrade(statusCode: StatusCode)
  final case class SimulatorActorPath(path: String)
}

class WindTurbineSimulator(id: String, endpoint: String)
                          (implicit materializer: ActorMaterializer)
  extends Actor with ActorLogging {
  implicit private val system = context.system
  implicit private val executionContext = system.dispatcher

  val webSocket = WebSocketClient(id, endpoint, self)

  override def postStop() = {
    log.info(s"$id : Stopping WebSocket connection")
    webSocket.killSwitch.shutdown()
  }

  override def receive: Receive = {
    case Upgraded =>
      log.info(s"$id : WebSocket upgraded")
    case FailedUpgrade(statusCode) =>
      log.error(s"$id : Failed to upgrade WebSocket connection : $statusCode")
      throw WindTurbineSimulatorException(id)
    case ConnectionFailure(ex) =>
      log.error(s"$id : Failed to establish WebSocket connection $ex")
      throw WindTurbineSimulatorException(id)
    case Connected =>
      log.info(s"$id : WebSocket connected")
      context.become(running)
  }

  def running: Receive = {
    case QueryActorPath =>
      sender ! SimulatorActorPath(context.self.path.toString)
    case Terminated =>
      log.error(s"$id : WebSocket connection terminated")
      throw WindTurbineSimulatorException(id)
  }
}

case class WindTurbineSimulatorException(id: String) extends Exception(id)