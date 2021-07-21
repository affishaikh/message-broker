package org.techninja.messagebroker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.techninja.messagebroker.log.AppendOnlyLog

@RestController
class TopicController(
    @Autowired val appendOnlyLog: AppendOnlyLog
) {

    @PostMapping("/topic")
    fun create(@RequestBody createTopicView: CreateTopicView): Boolean {
        return appendOnlyLog.create(createTopicView.topicName)
    }

    @PostMapping("/topic/{topicName}/append")
    fun append(
        @RequestBody appendToTopicView: AppendToTopicView,
        @PathVariable topicName: String
    ) {
        return appendOnlyLog.append(topicName, appendToTopicView.payload)
    }
}

data class CreateTopicView(
    val topicName: String
)

data class AppendToTopicView(
    val payload: String
)