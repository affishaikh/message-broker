package org.techninja.messagebroker.log

data class Record(
    val offset: Long,
    val payload: String
) {

    companion object {
        fun from(data: String): Record {

            return data.split(" ").let {
                val offset = it.first().toLong()
                val payload = it.subList(1, it.size).joinToString(" ")
                Record(offset = offset, payload = payload)
            }
        }
    }
}