package org.techninja.messagebroker.log

import org.techninja.messagebroker.exceptions.EmptyFileException
import org.techninja.messagebroker.service.Constants
import org.techninja.messagebroker.service.Constants.SIZE_OF_OFFSET
import org.techninja.messagebroker.service.FileIOService

class AppendOnlyLog(name: String) {
    private val logFile: FileIOService = FileIOService("${Constants.LOG_FILES_PATH}$name")
    private val indexFile: FileIOService = FileIOService("${Constants.LOG_FILES_PATH}$name.index")

    fun append(data: String) {
        val currentOffset = try {
            val lastLine = indexFile.getLastLine()
            OffsetPosition.from(lastLine).offset + 1
        } catch (e: EmptyFileException) {
            0
        }

        val newLineCharacter = getNewLineCharacter(currentOffset)
        val record = "$newLineCharacter${addPadding(currentOffset)} $data"
        val positionOfTheRecord = logFile.appendToFile(record)
        val positionOfMessage = positionOfTheRecord + SIZE_OF_OFFSET + 1
        val offsetPositionMapping = "$newLineCharacter${addPadding(currentOffset)} ${addPadding(positionOfMessage)}"

        indexFile.appendToFile(offsetPositionMapping)
    }

    fun readMessageFrom(physicalLocationOfMessage: Long): String {
        return logFile.readFromPhysicalLocationTillLineEnd(physicalLocationOfMessage)
    }

    private fun getNewLineCharacter(filePointer: Long) = if (filePointer == 0L) "" else '\n'

    private fun addPadding(currentOffset: Long) = currentOffset.toString().padStart(SIZE_OF_OFFSET, '0')
}
