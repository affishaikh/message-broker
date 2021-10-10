package org.techninja.messagebroker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.techninja.messagebroker.service.LogService
import reactor.core.publisher.Mono

@RestController
class LogController(
    @Autowired val logService: LogService
) {

    @PostMapping("/log")
    fun create(@RequestBody createLogView: CreateLogView): Mono<String> {
        return logService.createLog(createLogView.logName)
    }

    @PostMapping("/log/{logName}/append")
    fun append(
        @RequestBody appendToLogView: AppendToLogView,
        @PathVariable logName: String
    ): Mono<String> {
        return logService.appendToLog(logName, appendToLogView.payload)
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