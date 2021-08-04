package org.techninja.messagebroker.log

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RecordTest {

    @Test
    fun `should return the record`() {
        val data = "6 New data 1"

        Record.from(data) shouldBe Record(6, "New data 1")
    }
}