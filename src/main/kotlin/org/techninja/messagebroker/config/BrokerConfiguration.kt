package org.techninja.messagebroker.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("message-broker.broker-config")
data class BrokerConfiguration(
    val brokerId: Int,
    val host: String,
    val port: Int
)