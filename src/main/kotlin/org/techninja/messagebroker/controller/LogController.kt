package org.techninja.messagebroker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.techninja.messagebroker.log.AppendOnlyLog
import org.techninja.messagebroker.log.FileIO
import org.techninja.messagebroker.log.LOG_FILES_PATH
import org.techninja.messagebroker.service.LogService
import java.io.File

@RestController
class LogController(
    @Autowired val logService: LogService
) {

    @PostMapping("/log")
    fun create(@RequestBody createLogView: CreateLogView): Boolean {
        return File(LOG_FILES_PATH + createLogView.logName).createNewFile()
    }

    @PostMapping("/log/{logName}/append")
    fun append(
        @RequestBody appendToLogView: AppendToLogView,
        @PathVariable logName: String
    ) {
        val appendOnlyLog = AppendOnlyLog(FileIO.from(logName))

        return appendOnlyLog.append(appendToLogView.payload)
    }

    @GetMapping("/log/{logName}/{offset}")
    fun consume(
        @PathVariable logName: String,
        @PathVariable offset: Long
    ): String {
        return logService.consume(logName, offset)
    }
}

data class CreateLogView(
    val logName: String
)

data class AppendToLogView(
    val payload: String
)