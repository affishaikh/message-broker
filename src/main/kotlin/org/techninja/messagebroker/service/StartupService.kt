package org.techninja.messagebroker.service

import org.springframework.stereotype.Component
import org.techninja.messagebroker.config.BrokerConfiguration
import org.techninja.messagebroker.listeners.BrokerChangeListener
import org.techninja.messagebroker.service.Constants.BROKERS_PREFIX
import org.techninja.messagebroker.zookeeper_client.ZookeeperClient
import javax.annotation.PostConstruct

@Component
class StartupService(
    val zookeeperClient: ZookeeperClient,
    val brokerConfiguration: BrokerConfiguration,
    val brokerChangeListener: BrokerChangeListener
) {

    @PostConstruct
    fun start() {
        registerBrokerChangeListener()
        registerSelf()
    }

    private fun registerSelf() {
        val brokerPath = BROKERS_PREFIX + "/" + brokerConfiguration.brokerId
        val brokerData = ObjectMapperCache.objectMapper.writeValueAsString(Broker(
            id = brokerConfiguration.brokerId,
            host = brokerConfiguration.host,
            port = brokerConfiguration.port
        ))
        zookeeperClient.createEphemeralPath(brokerPath, brokerData)
    }

    private fun registerBrokerChangeListener() {
        zookeeperClient.registerListener(BROKERS_PREFIX, brokerChangeListener)
    }
}

data class Broker(
    val id: Int,
    val host: String,
    val port: Int
)