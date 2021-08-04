package org.techninja.messagebroker.service

import org.techninja.messagebroker.exceptions.EmptyFileException
import org.techninja.messagebroker.log.LOG_FILES_PATH
import java.io.RandomAccessFile

class FileIOService(fileName: String) : RandomAccessFile(fileName, "rw") {

    fun getLastLine(): String {
        val fileLength = this.length() - 1
        if (fileLength < 0) {
            throw EmptyFileException()
        }

        val sb = StringBuilder()
        for (filePointer in (this.length() - 1) downTo 0) {
            this.seek(filePointer)
            val readByte = this.readByte().toInt()
            if (readByte == 0xA) {
                break
            } else if (readByte == 0xD) {
                if (filePointer == fileLength - 1) {
                    continue
                }
                break
            }
            sb.append(readByte.toChar())
        }
        return sb.reverse().toString()
    }

    fun appendToFile(record: String) {
        this.seek(this.length() - 1)
        this.writeBytes(record)
    }

    fun readFromPhysicalLocationTillLineEnd(physicalLocationOfMessage: Long): String {
        val sb = StringBuilder()
        var filePointer = physicalLocationOfMessage

        this.seek(filePointer)
        var nextInt = this.readByte().toInt()
        while (!isEndOfLine(nextInt)) {
            sb.append(nextInt.toChar())
            this.seek(++filePointer)
            nextInt = this.readByte().toInt()
        }

        println("Message $sb")
        return sb.toString()
    }

    private fun isEndOfLine(nextInt: Int) = nextInt == 0xA || nextInt == 0xD

    companion object {
        fun from(logName: String) = FileIOService(LOG_FILES_PATH + logName)
    }
}