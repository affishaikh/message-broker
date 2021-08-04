package org.techninja.messagebroker.log

import org.techninja.messagebroker.exceptions.EmptyFileException
import org.techninja.messagebroker.service.FileIOService
import java.io.File

const val LOG_FILES_PATH = "./logs/"

class AppendOnlyLog(name: String) {
    private val fileIOService: FileIOService = FileIOService("${LOG_FILES_PATH}$name")

    fun append(data: String) {
        val currentOffset = try {
            val lastLine = fileIOService.getLastLine()
            Record.from(lastLine).offset + 1
        } catch (e: EmptyFileException) {
            0
        }

        val record = if (currentOffset == 0L) {
            "$currentOffset $data"
        } else {
            "\n$currentOffset $data"
        }
        fileIOService.appendToFile(record)
    }

    fun create(logName: String): Boolean {
        return File(LOG_FILES_PATH + logName).createNewFile()
    }

    fun readMessageFrom(physicalLocationOfMessage: Long): String {

        return fileIOService.readFromPhysicalLocationTillLineEnd(physicalLocationOfMessage)
    }
}

data class Record(
    val offset: Long,
    val payload: String
) {

    companion object {
        fun from(data: String): Record {

            return data.split(" ").let {
                val offset = it.first().toLong()
                val payload = it.subList(1, it.size).joinToString(" ")
                Record(offset = offset, payload = payload)
            }
        }
    }
}

