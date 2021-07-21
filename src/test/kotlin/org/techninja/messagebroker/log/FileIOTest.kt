package org.techninja.messagebroker.log

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.util.ResourceUtils
import org.techninja.messagebroker.exceptions.EmptyFileException
import java.io.File

class FileIOTest {

    @Test
    fun `should throw empty file exception`() {
        val fileIO = FileIO(File("./src/test/resources/emptyLogFile"))

        shouldThrow<EmptyFileException> {
            fileIO.getLastLine()
        }
    }
}