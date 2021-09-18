package org.techninja.messagebroker.service

import org.techninja.messagebroker.service.Constants.LOG_FILES_PATH
import org.techninja.messagebroker.service.Constants.SIZE_OF_OFFSET

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
