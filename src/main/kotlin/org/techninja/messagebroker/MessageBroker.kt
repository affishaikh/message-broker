package org.techninja.messagebroker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class MessageBroker

fun main(args: Array<String>) {
	runApplication<MessageBroker>(*args)
}
