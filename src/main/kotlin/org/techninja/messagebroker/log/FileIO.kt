package org.techninja.messagebroker.log

import org.techninja.messagebroker.exceptions.EmptyFileException
import java.io.File
import java.io.RandomAccessFile

class FileIO(
    private val file: File
) : RandomAccessFile(file, "r") {

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
        file.appendBytes(record.toByteArray())
    }

    fun readFromTillLineEnd(physicalLocationOfMessage: Long): String {
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
        fun from(logName: String) = FileIO(File(LOG_FILES_PATH + logName))
    }
}