package org.techninja.messagebroker.service

import java.io.RandomAccessFile

const val SIZE_OF_OFFSET = 2

class IndexFileIOService(fileName: String) : RandomAccessFile(fileName, "r") {

    fun getPhysicalLocationFor(offset: Long): Long {

        val physicalLocation = (SIZE_OF_OFFSET + 1) * ((offset + 1) * 2 - 1)
        val sb = StringBuilder()
        for (filePointer in (physicalLocation..(physicalLocation + (SIZE_OF_OFFSET - 1)))) {
            this.seek(filePointer)
            sb.append(this.readByte().toInt().toChar())
        }

        return sb.toString().toLong()
    }
}
