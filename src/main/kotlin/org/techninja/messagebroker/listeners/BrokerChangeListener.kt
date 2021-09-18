package org.techninja.messagebroker.listeners

import org.I0Itec.zkclient.IZkChildListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.techninja.messagebroker.service.Brokers
import org.techninja.messagebroker.zookeeper_client.ZookeeperClient

@Component
class BrokerChangeListener(
    val brokers: Brokers
) : IZkChildListener {
    private val logger = LoggerFactory.getLogger(ZookeeperClient::class.java)

    override fun handleChildChange(parentPath: String, currentChilds: MutableList<String>) {
        logger.info("Received broker change for path $parentPath and brokers are $currentChilds")

        brokers.add(currentChilds - brokers.currentBrokerIds())
        brokers.remove(brokers.currentBrokerIds() - currentChilds)
    }
}