package org.techninja.messagebroker.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.techninja.messagebroker.zookeeper_client.ZookeeperClient

@Component
class Brokers(
    val zookeeperClient: ZookeeperClient
) {
    private val logger = LoggerFactory.getLogger(ZookeeperClient::class.java)
    private val availableBrokers = mutableListOf<Broker>()

    fun currentBrokerIds() = availableBrokers.map { it.id.toString() }

    fun add(newlyAddedBrokers: List<String>) {
        if (newlyAddedBrokers.isEmpty()) return

        newlyAddedBrokers.map {
            zookeeperClient.getBrokerMetaData(it)
        }.let {
            availableBrokers.addAll(it)
        }
        logger.info("Newly added brokers are $newlyAddedBrokers. All brokers ${currentBrokerIds()}")
    }

    fun remove(removedBrokerIds: List<String>) {
        if (removedBrokerIds.isEmpty()) return

        availableBrokers.removeIf { removedBrokerIds.contains(it.id.toString()) }
        logger.info("Removed brokers are $removedBrokerIds. All brokers ${currentBrokerIds()}")
    }
}