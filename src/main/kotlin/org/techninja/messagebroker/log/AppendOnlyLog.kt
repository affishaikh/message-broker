package org.techninja.messagebroker.log

import org.springframework.stereotype.Component
import org.techninja.messagebroker.exceptions.EmptyFileException
import java.io.File

const val LOG_FILES_PATH = "./logs/"

@Component
class AppendOnlyLog {

    fun append(logName: String, data: String) {
        val fileIO = FileIO(File(LOG_FILES_PATH + logName))
        val currentOffset = try {
            val lastLine = fileIO.getLastLine()
            Record.from(lastLine).offset + 1
        } catch (e: EmptyFileException) {
            0
        }

        val record = if (currentOffset == 0L) {
            "$currentOffset $data"
        } else {
            "\n$currentOffset $data"
        }
        fileIO.appendToFile(record)
    }

    fun create(logName: String): Boolean {
        return File(LOG_FILES_PATH + logName).createNewFile()
    }
}

data class Record(
    val offset: Long,
    val payload: String
) {

    companion object {
        fun from(data: String): Record {
            val indexOfFirst = data.indexOfFirst { it == ' ' }
            val offset = data.subSequence(0, indexOfFirst).toString().toLong()
            val payload = data.subSequence(indexOfFirst + 1, data.length).toString()
            return Record(offset = offset, payload = payload)
        }
    }
}

