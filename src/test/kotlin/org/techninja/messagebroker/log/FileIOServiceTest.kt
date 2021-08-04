package org.techninja.messagebroker.log

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.techninja.messagebroker.exceptions.EmptyFileException
import org.techninja.messagebroker.service.FileIOService

class FileIOServiceTest {

    @Test
    fun `should throw empty file exception`() {
        val fileIO = FileIOService("./src/test/resources/logs/emptyLogFile")

        shouldThrow<EmptyFileException> {
            fileIO.getLastLine()
        }
    }

    @Test
    fun `should read the next line`() {
        val fileIO = FileIOService("./src/test/resources/logs/someLogs")

        fileIO.readFromPhysicalLocationTillLineEnd(17) shouldBe "hello world!"
    }
}