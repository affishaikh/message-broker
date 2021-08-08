package org.techninja.messagebroker.log

import org.techninja.messagebroker.exceptions.EmptyFileException
import org.techninja.messagebroker.service.FileIOService

const val LOG_FILES_PATH = "./logs/"

class AppendOnlyLog(name: String) {
    private val logFile: FileIOService = FileIOService("${LOG_FILES_PATH}$name")
    private val indexFile: FileIOService = FileIOService("${LOG_FILES_PATH}$name.index")

    fun append(data: String) {
        val currentOffset = try {
            val lastLine = indexFile.getLastLine()
            OffsetPosition.from(lastLine).offset + 1
        } catch (e: EmptyFileException) {
            0
        }.toString().padStart(8, '0')

        val record = if (currentOffset == "0".padStart(8, '0')) {
            "$currentOffset $data"
        } else {
            "\n$currentOffset $data"
        }
        val positionOfTheRecord = logFile.appendToFile(record)
        val positionOfMessage = (positionOfTheRecord + currentOffset.length + 1).toString().padStart(8, '0')

        val offsetPositionMapping = if (currentOffset == "0".padStart(8, '0')) {
            "$currentOffset $positionOfMessage"
        } else {
            "\n$currentOffset $positionOfMessage"
        }

        indexFile.appendToFile(offsetPositionMapping)
    }

    fun readMessageFrom(physicalLocationOfMessage: Long): String {

        return logFile.readFromPhysicalLocationTillLineEnd(physicalLocationOfMessage)
    }
}
