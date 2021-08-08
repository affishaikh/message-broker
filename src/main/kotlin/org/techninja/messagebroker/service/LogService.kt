package org.techninja.messagebroker.service

import org.springframework.stereotype.Component
import org.techninja.messagebroker.log.AppendOnlyLog
import org.techninja.messagebroker.log.LOG_FILES_PATH
import java.io.File

@Component
class LogService {

    fun consume(logName: String, offset: Long): String {
        val physicalLocationOfMessage = IndexFileIOService(logName)
            .getPhysicalLocationFor(offset)

        println("Physical location --------> $physicalLocationOfMessage <------------")

        val appendOnlyLog = AppendOnlyLog(logName)

        return appendOnlyLog.readMessageFrom(physicalLocationOfMessage)
    }

    fun appendToLog(logName: String, payload: String) {
        val appendOnlyLog = AppendOnlyLog(logName)

        return appendOnlyLog.append(payload)
    }

    fun createLog(logName: String) {
        File("$LOG_FILES_PATH$logName").createNewFile()
        File("$LOG_FILES_PATH$logName.index").createNewFile()
    }
}