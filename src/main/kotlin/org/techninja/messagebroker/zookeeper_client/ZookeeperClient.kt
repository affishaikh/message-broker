package org.techninja.messagebroker.zookeeper_client

import org.I0Itec.zkclient.IZkChildListener
import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.techninja.messagebroker.config.ZookeeperEnsembleConfiguration
import org.techninja.messagebroker.service.Broker
import org.techninja.messagebroker.service.Constants.BROKERS_PREFIX
import org.techninja.messagebroker.service.ObjectMapperCache

@Component
class ZookeeperClient(
    zookeeperEnsembleConfiguration: ZookeeperEnsembleConfiguration
) {
    private val zkClient = ZkClient(zookeeperEnsembleConfiguration.ensembleConnectionString)
    private val logger = LoggerFactory.getLogger(ZookeeperClient::class.java)

    fun createEphemeralPath(brokerPath: String, brokerData: String) {
        try {
            zkClient.createEphemeral(brokerPath, brokerData)
            logger.info("Created Ephemeral path for $brokerPath with data: $brokerData")
        } catch (zkNoNodeException: ZkNoNodeException) {
            zkClient.createPersistent(BROKERS_PREFIX)
            logger.info("Created persistent path for $BROKERS_PREFIX")

            zkClient.createEphemeral(brokerPath, brokerData)
            logger.info("Created Ephemeral path for $brokerPath with data: $brokerData")
        }
    }

    fun registerListener(path: String, listener: IZkChildListener) {
        zkClient.subscribeChildChanges(path, listener)
        logger.info("Subscribed listener for path $path ")
    }

    fun getBrokerMetaData(brokerId: String): Broker {
        val broker = zkClient.readData<String>(createBrokerPath(brokerId))
        logger.info("Fetched broker metadata from zookeeper $broker ")

        return ObjectMapperCache.objectMapper.readValue(broker, Broker::class.java)
    }

    private fun createBrokerPath(brokerId: String) = "$BROKERS_PREFIX/$brokerId"
}