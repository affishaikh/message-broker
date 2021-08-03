package org.techninja.messagebroker.log

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.techninja.messagebroker.exceptions.EmptyFileException
import java.io.File

class FileIOTest {

    @Test
    fun `should throw empty file exception`() {
        val fileIO = FileIO(File("./src/test/resources/logs/emptyLogFile"))

        shouldThrow<EmptyFileException> {
            fileIO.getLastLine()
        }
    }

    @Test
    fun `should read the next line`() {
        val fileIO = FileIO(File("./src/test/resources/logs/someLogs"))

        fileIO.readFromTillLineEnd(17) shouldBe "hello world!"
    }
}