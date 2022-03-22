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
internal class FBUserControllerTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    internal fun `deny a user with an unsupported gender`() {
        val request = """
            {                        
                        "name": "test 1234",
                        "gender": "dolphin"
            }
        """.trimMargin()

        webTestClient.post().uri("/fb-users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .jsonPath("$.violations.length()").isEqualTo(1)
            .jsonPath("$.violations[0].field").isEqualTo("gender")
            .jsonPath("$.violations[0].message").isEqualTo("Must be one of [male, female, cis, non-binary]")
    }

    @Test
    internal fun `deny a user with a missing gender`() {
        val request = """
            {                        
                        "name": "test 1234"
            }
        """.trimMargin()

        webTestClient.post().uri("/fb-users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .jsonPath("$.violations.length()").isEqualTo(1)
            .jsonPath("$.violations[0].field").isEqualTo("gender")
            .jsonPath("$.violations[0].message").isEqualTo("Must be one of [male, female, cis, non-binary]")
    }

    @Test
    internal fun `accept a user fitting the constraints`() {
        val request = """
            {                        
                        "name": "test 1234",
                        "gender": "cis"
            }
        """.trimMargin()

        webTestClient.post().uri("/fb-users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .consumeWith {
                val body= String(it.responseBody!!)
                assertThat(body).isEqualTo("FB User is valid")
            }
    }
}