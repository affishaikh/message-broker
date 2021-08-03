package org.techninja.messagebroker.service

import org.springframework.stereotype.Component
import org.techninja.messagebroker.log.AppendOnlyLog
import org.techninja.messagebroker.log.FileIO
import org.techninja.messagebroker.log.LOG_FILES_PATH

@Component
class LogService {

    fun consume(logName: String, offset: Long): String {
        val physicalLocationOfMessage = IndexFileIOService("$LOG_FILES_PATH$logName.index")
            .getPhysicalLocationFor(offset)

        println("Physical location --------> $physicalLocationOfMessage <------------")

        val appendOnlyLog = AppendOnlyLog(FileIO.from(logName))

        return appendOnlyLog.readMessageFrom(physicalLocationOfMessage)
    }
}