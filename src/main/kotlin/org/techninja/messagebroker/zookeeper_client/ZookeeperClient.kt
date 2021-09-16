package org.techninja.messagebroker.zookeeper_client

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.springframework.stereotype.Component
import org.techninja.messagebroker.config.ZookeeperEnsembleConfiguration

@Component
class ZookeeperClient(
    zookeeperEnsembleConfiguration: ZookeeperEnsembleConfiguration
) {
    private val brokersPrefix = "/brokers"
    private val zkClient = ZkClient(zookeeperEnsembleConfiguration.ensembleConnectionString)

    fun createEphemeralPath(brokerPath: String, brokerData: String) {
        try {
            zkClient.createEphemeral(brokerPath, brokerData)
        } catch (zkNoNodeException: ZkNoNodeException) {
            zkClient.createPersistent(brokersPrefix)
            zkClient.createEphemeral(brokerPath, brokerData)
        }
    }
}