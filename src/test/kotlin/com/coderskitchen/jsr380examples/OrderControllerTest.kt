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
    internal fun `deny an order with an adult item shipped to a child`() {
        val request = """
            {
             "containsAdultItems": true,
             "receiver": {          
               "age": 17,
               "name": "tester"             
             }              
            }
        """.trimMargin()

        webTestClient.post().uri("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .jsonPath("$.violations.length()").isEqualTo(1)
            .jsonPath("$.violations[0].field").isEqualTo("order")
            .jsonPath("$.violations[0].message").isEqualTo("This order requires an adult receiver")
    }

    @Test
    internal fun `accepts an order without an adult item shipped to a child`() {
        val request = """
            {
             "containsAdultItems": false,
             "receiver": {          
               "age": 17,
               "name": "tester"             
             }              
            }
        """.trimMargin()

        webTestClient.post().uri("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isNoContent
    }

    @Test
    internal fun `accepts an order with an item shipped to an adult`() {
        val request = """
            {
             "containsAdultItems": true,
             "receiver": {          
               "age": 18,
               "name": "tester"             
             }              
            }
        """.trimMargin()

        webTestClient.post().uri("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isNoContent
    }
}