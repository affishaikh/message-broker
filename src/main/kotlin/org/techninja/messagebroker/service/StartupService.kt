package org.techninja.messagebroker.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.techninja.messagebroker.config.BrokerConfiguration
import org.techninja.messagebroker.zookeeper_client.ZookeeperClient
import javax.annotation.PostConstruct

@Component
class StartupService(
    val zookeeperClient: ZookeeperClient,
    val brokerConfiguration: BrokerConfiguration
) {

    private val brokersPrefix = "/brokers"

    @PostConstruct
    fun start() {
        registerSelf()
    }

    private fun registerSelf() {
        val brokerPath = brokersPrefix + "/" + brokerConfiguration.brokerId
        val brokerData = ObjectMapper().writeValueAsString(Broker(
            id = brokerConfiguration.brokerId,
            host = brokerConfiguration.host,
            port = brokerConfiguration.port
        ))
        zookeeperClient.createEphemeralPath(brokerPath, brokerData)
    }
}

data class Broker(
    val id: Int,
    val host: String,
    val port: Int
)