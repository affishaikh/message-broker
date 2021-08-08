package org.techninja.messagebroker.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.techninja.messagebroker.exceptions.EmptyFileException

class FileIOServiceTest {

    @Test
    fun `should throw empty file exception`() {
        val fileIO = FileIOService("./src/test/resources/logs/emptyLogFile")

        shouldThrow<EmptyFileException> {
            fileIO.getLastLine()
        }
    }

    @Test
    fun `should read the first record`() {
        val fileIO = FileIOService("./src/test/resources/logs/someLogs")

        fileIO.readFromPhysicalLocationTillLineEnd(9) shouldBe "hello world!"
    }

    @Test
    fun `should read the second record`() {

        val fileIO = FileIOService("./src/test/resources/logs/someLogs")

        fileIO.readFromPhysicalLocationTillLineEnd(31) shouldBe "some new data"
    }
}