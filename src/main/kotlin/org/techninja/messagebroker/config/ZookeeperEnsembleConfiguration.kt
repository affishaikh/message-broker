package org.techninja.messagebroker.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("message-broker.zookeeper-config")
data class ZookeeperEnsembleConfiguration(
    val ensembleConnectionString: String
)