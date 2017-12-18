package com.abtechsoft

import akka.cluster.sharding.ShardRegion

final case class EntityEnvelope(id: String, payload: Any)

object WindTurbineClusterConfig {
  private val numberOfShards = 100

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case EntityEnvelope(id, payload) => (id, payload)
  }

  val extractShardId: ShardRegion.ExtractShardId = {
    case EntityEnvelope(id, _) => (id.hashCode % numberOfShards).toString
    case ShardRegion.StartEntity(id) => (id.hashCode % numberOfShards).toString
  }
}