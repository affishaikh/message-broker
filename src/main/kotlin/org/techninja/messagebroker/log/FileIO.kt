package org.techninja.messagebroker.log

import java.io.File
import java.io.RandomAccessFile

class FileIO(
    private val file: File
) {

    private val randomAccessFile = RandomAccessFile(file, "r")

    fun getLastLine(): String {
        val fileLength = randomAccessFile.length() - 1
        val sb = StringBuilder()
        for (filePointer in (randomAccessFile.length() - 1) downTo 0) {
            randomAccessFile.seek(filePointer)
            val readByte = randomAccessFile.readByte().toInt()
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
}