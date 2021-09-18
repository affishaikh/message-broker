package org.techninja.messagebroker.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object ObjectMapperCache {
    val objectMapper = jacksonObjectMapper()
}