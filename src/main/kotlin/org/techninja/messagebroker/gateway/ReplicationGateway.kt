package org.techninja.messagebroker.gateway

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.techninja.messagebroker.controller.AppendToLogView
import org.techninja.messagebroker.controller.CreateLogView
import org.techninja.messagebroker.service.Broker
import reactor.core.publisher.Mono

@Component
class ReplicationGateway(
    webClient: WebClient.Builder
) {
    private val webClient = webClient.build()

    fun createLog(createLogView: CreateLogView, broker: Broker): Mono<String> {
        return webClient.post()
            .uri {
                it.host(broker.host).port(broker.port).path("/log").build()
            }
            .bodyValue(createLogView)
            .retrieve()
            .bodyToMono(String::class.java)
    }

    fun appendToLog(logName: String, appendToLogView: AppendToLogView, broker: Broker): Mono<String> {
        return webClient.post()
            .uri {
                it.host(broker.host).port(broker.port).path("/log/$logName/append").build()
            }
            .bodyValue(appendToLogView)
            .retrieve()
            .bodyToMono(String::class.java)
    }
}