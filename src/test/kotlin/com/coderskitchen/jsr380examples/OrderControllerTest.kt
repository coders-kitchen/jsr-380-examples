package com.coderskitchen.jsr380examples

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class OrderControllerTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    internal fun `deny an order using a pricelist not available to the user`() {

        val request = """
            {
                        "selectedPriceList": 18,
                        "items": ["sample", "test"]
            }
        """.trimMargin()

        webTestClient.post().uri("/orders")
            .header("x-user-id", "123")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .jsonPath("$.violations.length()").isEqualTo(1)
            .jsonPath("$.violations[0].field").isEqualTo("submitOrder.<cross-parameter>.selectedPriceList")
            .jsonPath("$.violations[0].message").isEqualTo("Chosen price list id 18 is not available to you")
    }
}