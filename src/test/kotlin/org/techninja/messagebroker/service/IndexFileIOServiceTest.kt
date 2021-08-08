package org.techninja.messagebroker.service

import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IndexFileIOServiceTest {

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return the physical location of message for the given offset`() {

        mockkConstructor(FileIOService::class)

        every {
            anyConstructed<FileIOService>().readFromPhysicalLocationTillLineEnd(any())
        } returns "1"

        val indexFileIOService = IndexFileIOService("someLogs")

        indexFileIOService.getPhysicalLocationFor(1) shouldBe 1L

        verify {
            anyConstructed<FileIOService>().readFromPhysicalLocationTillLineEnd(27)
        }
    }
}