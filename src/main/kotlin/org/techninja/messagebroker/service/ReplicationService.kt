package org.techninja.messagebroker.service

import org.springframework.stereotype.Component
import org.techninja.messagebroker.controller.AppendToLogView
import org.techninja.messagebroker.controller.CreateLogView
import org.techninja.messagebroker.gateway.ReplicationGateway
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ReplicationService(
    private val brokers: Brokers,
    private val replicationGateway: ReplicationGateway
) {

    fun createLog(logName: String): Mono<String> {
        return Flux.fromIterable(brokers.getAvailableBrokers()).flatMap {
            replicationGateway.createLog(CreateLogView(logName), it)
        }.collectList().map {
            it.first()
        }
    }

    fun appendToLog(logName: String, appendToLogView: AppendToLogView): Mono<String> {
        return Flux.fromIterable(brokers.getAvailableBrokers()).flatMap {
            replicationGateway.appendToLog(logName, appendToLogView, it)
        }.collectList().map {
            it.first()
        }
    }
}