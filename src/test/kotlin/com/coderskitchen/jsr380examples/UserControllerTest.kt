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
internal class UserControllerTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    internal fun `executes malicious code`() {
        val maliciousName = "$" + "{\\\"\\\".getClass()" +
                ".forName(\\\"java.net.http.HttpClient\\\")" +
                ".getDeclaredMethods()[2].toString()}"
        val request = """
            {
                        
                        "name": "$maliciousName"
            }
        """.trimMargin()

        webTestClient.post().uri("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .jsonPath("$.violations.length()").isEqualTo(1)
            .jsonPath("$.violations[0].field").isEqualTo("name")
            .jsonPath("$.violations[0].message").isEqualTo("'public abstract java.util.Optional java.net.http.HttpClient.executor()' does not match the naming conventions (^[A-Z].*)")
    }
}