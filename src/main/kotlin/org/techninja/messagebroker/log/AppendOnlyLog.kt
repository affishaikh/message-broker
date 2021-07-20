package org.techninja.messagebroker.log

import java.io.File

class AppendOnlyLog  {

    fun append(logName: String, data: String) {
        val fileIO = FileIO(File(logName))
        val lastLine = fileIO.getLastLine()
        val currentOffset = Record.from(lastLine).offset
        val record = "$currentOffset $data"
        fileIO.appendToFile(record)
    }

    fun create(logName: String): Boolean {
        return File(logName).createNewFile()
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

