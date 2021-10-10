package org.techninja.messagebroker.service

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.techninja.messagebroker.controller.CreateLogView
import org.techninja.messagebroker.gateway.ReplicationGateway
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class ReplicationServiceTest {

    private val brokers = mockk<Brokers>()
    private val replicationGateway = mockk<ReplicationGateway>()
    private val replicationService = ReplicationService(brokers, replicationGateway)

    @BeforeEach
    fun setUp() { clearAllMocks() }

    @AfterEach
    fun tearDown() { clearAllMocks() }

    @Test
    fun `should replicate the created log to available brokers`() {
        every { brokers.getAvailableBrokers() } returns listOf(
            Broker(id = 0, host = "", port = 0),
            Broker(id = 0, host = "", port = 0)
        )
        every { replicationGateway.createLog(any(), any()) } returns Mono.just("payments")

        StepVerifier.create(replicationService.createLog("payments"))
            .expectNext("payments")
            .verifyComplete()

        verify(exactly = 2) {
            replicationGateway.createLog(CreateLogView("payments"), Broker(id = 0, host = "", port = 0))
        }
    }
}