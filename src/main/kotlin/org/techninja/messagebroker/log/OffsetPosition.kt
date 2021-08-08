package org.techninja.messagebroker.log

data class OffsetPosition(
    val offset: Long,
    val position: Long
) {

    companion object {
        fun from(data: String): OffsetPosition {

            return data.split(" ").let {
                val offset = it.first().toLong()
                val position = it[1].toLong()
                OffsetPosition(offset = offset, position = position)
            }
        }
    }
}