package org.techninja.messagebroker.service

import org.techninja.messagebroker.exceptions.EmptyFileException
import java.io.RandomAccessFile

const val NEW_LINE_CHARACTER = '\n'
const val CARRIAGE_RETURN_CHARACTER = '\r'

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

    fun appendToFile(data: String): Long {
        val filePointer = this.length()
        if (filePointer < 0)
            this.seek(0)
        else
            this.seek(filePointer)

        this.writeBytes(data)
        return filePointer
    }

    fun readFromPhysicalLocationTillLineEnd(physicalLocationOfMessage: Long): String {
        val data = StringBuilder()
        var filePointer = physicalLocationOfMessage

        do {
            val nextInt = readCharAt(filePointer)
            data.append(nextInt)
            ++filePointer
        } while (!isEOF(filePointer) && !isEndOfLine(readCharAt(filePointer)))

        return data.toString()
    }

    private fun isEOF(filePointer: Long) = this.length() == filePointer

    private fun readCharAt(filePointer: Long): Char {
        this.seek(filePointer)
        return this.readByte().toChar()
    }

    private fun isEndOfLine(character: Char) = character == NEW_LINE_CHARACTER || character == CARRIAGE_RETURN_CHARACTER
}