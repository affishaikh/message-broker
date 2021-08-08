package org.techninja.messagebroker.service

import org.techninja.messagebroker.log.LOG_FILES_PATH

const val SIZE_OF_OFFSET = 8

class IndexFileIOService(
    logName: String,
) {
    private val fileIOService: FileIOService = FileIOService("$LOG_FILES_PATH$logName.index")

    fun getPhysicalLocationFor(offset: Long): Long {

        val physicalLocation = getPhysicalLocationOfOffset(offset)

        return fileIOService.readFromPhysicalLocationTillLineEnd(physicalLocation).toLong()
    }

    private fun getPhysicalLocationOfOffset(offset: Long) = (SIZE_OF_OFFSET + 1) * ((offset + 1) * 2 - 1)
}
