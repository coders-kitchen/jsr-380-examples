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
    internal fun `deny a user with an unsupported source`() {
        val request = """
            {
                        "source": "not known"
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
            .jsonPath("$.violations[0].field").isEqualTo("source")
            .jsonPath("$.violations[0].message").isEqualTo("Only direct or google registration is supported")
    }

    @ParameterizedTest
    @ValueSource(ints = [4, 31])
    internal fun `deny a user registered directly with a name not fitting into length restrictions`(length: Int) {
        val name = "a".repeat(length)
        val request = """
            {
                        "source": "direct",
                        "age": 18,
                        "name": "$name"
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
            .jsonPath("$.violations[0].message").isEqualTo("Name must be between 5 and 30 characters")
    }

    @ParameterizedTest
    @ValueSource(ints = [9, 121])
    internal fun `deny a user registered via google with a name not fitting into length restrictions`(length: Int) {
        val name = "a".repeat(length)
        val request = """
            {
                        "source": "google",
                        "name": "$name",
                        "profileSource": "https://test.de"
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
            .jsonPath("$.violations[0].message").isEqualTo("Name must be between 10 and 120 characters")
    }

    @ParameterizedTest
    @ValueSource(ints = [17, 151])
    internal fun `deny a user to old or to young`(age: Int) {
        val request = """
            {
                        "source": "direct",
                        "age": $age,
                        "name": "test 1234"
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
            .jsonPath("$.violations[0].field").isEqualTo("age")
            .jsonPath("$.violations[0].message").isEqualTo("Age must be between 18 and 150")
    }

    @Test
    internal fun `deny a user with an too extensive about me`() {
        val aboutMe = "a".repeat(201)
        val request = """
            {
                        "source": "direct",            
                        "age": 18,
                        "aboutMe": "$aboutMe",
                        "name": "test 1234"
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
            .jsonPath("$.violations[0].field").isEqualTo("aboutMe")
            .jsonPath("$.violations[0].message").isEqualTo("About Me must be between 0 and 200 characters")
    }

    @Test
    internal fun `deny a user registered via google with an invalid url`() {
        val request = """
            {
                        "source": "google",
                        "name": "test user 123",
                        "profileSource": "jhjskfdf:sdfjhksdfkjhsdf"
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
            .jsonPath("$.violations[0].field").isEqualTo("profileSource")
            .jsonPath("$.violations[0].message").isEqualTo("must be a valid URL")
    }

    @Test
    internal fun `accept a user registered directly`() {
        val request = """
            {
                        "source": "direct",
                        "age": 18,
                        "aboutMe": "this is from the test",
                        "name": "test 1234"
            }
        """.trimMargin()

        webTestClient.post().uri("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isAccepted
            .expectBody()
            .consumeWith {
                val body= String(it.responseBody!!)
                assertThat(body).isEqualTo("User is valid and registered via direct")
            }
    }

    @Test
    internal fun `accept a user registered via google`() {
        val request = """
            {
                        "source": "google",
                        "name": "test user 123",
                        "profileSource": "https://google.de"
            }
        """.trimMargin()

        webTestClient.post().uri("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isAccepted
            .expectBody()
            .consumeWith {
                val body= String(it.responseBody!!)
                assertThat(body).isEqualTo("User is valid and registered via google")
            }
    }
}