package org.techninja.messagebroker.service

import org.springframework.stereotype.Component
import org.techninja.messagebroker.controller.AppendToLogView
import org.techninja.messagebroker.log.AppendOnlyLog
import org.techninja.messagebroker.service.Constants.LOG_FILES_PATH
import reactor.core.publisher.Mono
import java.io.File

@Component
class LogService(
    private val replicationService: ReplicationService
) {

    fun consume(logName: String, offset: Long): String {
        val physicalLocationOfMessage = IndexFileIOService(logName)
            .getPhysicalLocationFor(offset)

        println("Physical location --------> $physicalLocationOfMessage <------------")

        val appendOnlyLog = AppendOnlyLog(logName)

        return appendOnlyLog.readMessageFrom(physicalLocationOfMessage)
    }

    fun appendToLog(logName: String, payload: String): Mono<String> {
        val appendOnlyLog = AppendOnlyLog(logName)
        appendOnlyLog.append(payload)

        return replicationService.appendToLog(logName, AppendToLogView(payload))
    }

    fun createLog(logName: String): Mono<String> {
        File("$LOG_FILES_PATH$logName").createNewFile()
        File("$LOG_FILES_PATH$logName.index").createNewFile()

        return replicationService.createLog(logName)
    }
}