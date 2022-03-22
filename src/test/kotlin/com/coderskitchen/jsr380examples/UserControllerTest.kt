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

    @ParameterizedTest
    @ValueSource(ints = [4, 31])
    internal fun `deny a user name not fitting into length restrictions`(length: Int) {
        val name = "a".repeat(length)
        val request = """
            {
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
    @ValueSource(ints = [17, 151])
    internal fun `deny a user to old or to young`(age: Int) {
        val request = """
            {
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

    @ParameterizedTest
    @ValueSource(ints = [4, 11])
    internal fun `deny a user with a website description not fitting into length restrictions`(length:Int) {

        val description = "a".repeat(length)

        val request = """
            {
                        "age": 18,
                        "aboutMe": "nothing",
                        "name": "test 1234",
                        "websites": {
                          "$description": "https://test.de"
                        }
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
            .jsonPath("$.violations[0].field").isEqualTo("websites[$description]")
            .jsonPath("$.violations[0].message").isEqualTo("size must be between 5 and 10")
    }

    @Test
    internal fun `deny a user invalid website url`() {
        val request = """
            {
                        "age": 18,
                        "aboutMe": "nothing",
                        "name": "test 1234",
                        "websites": {
                          "sample": "tcp://1232"
                        }
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
            .jsonPath("$.violations[0].field").isEqualTo("websites[sample]")
            .jsonPath("$.violations[0].message").isEqualTo("must be a valid URL")
    }

    @ParameterizedTest
    @ValueSource(ints = [4, 11])
    internal fun `deny a user with a address description not fitting into length restrictions`(length:Int) {

        val description = "a".repeat(length)

        val request = """
            {
                        "age": 18,
                        "aboutMe": "nothing",
                        "name": "test 1234",
                        "addresses": {
                          "$description": {
                            "street": "sample street",
                            "city": "sample city"
                          }
                        }
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
            .jsonPath("$.violations[0].field").isEqualTo("addresses[$description]")
            .jsonPath("$.violations[0].message").isEqualTo("size must be between 5 and 10")
    }

    @ParameterizedTest
    @ValueSource(ints = [4, 31])
    internal fun `deny a user with a street address not fitting into length restrictions`(length:Int) {

        val street = "a".repeat(length)

        val request = """
            {
                        "age": 18,
                        "aboutMe": "nothing",
                        "name": "test 1234",
                        "addresses": {
                          "sample": {
                            "street": "$street",
                            "city": "sample city"
                          }
                        }
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
            .jsonPath("$.violations[0].field").isEqualTo("addresses[sample].street")
            .jsonPath("$.violations[0].message").isEqualTo("size must be between 5 and 30")
    }

    @ParameterizedTest
    @ValueSource(ints = [4, 16])
    internal fun `deny a user with a city not fitting into length restrictions`(length:Int) {

        val city = "a".repeat(length)

        val request = """
            {
                        "age": 18,
                        "aboutMe": "nothing",
                        "name": "test 1234",
                        "addresses": {
                          "sample": {
                            "street": "sample street",
                            "city": "$city"
                          }
                        }
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
            .jsonPath("$.violations[0].field").isEqualTo("addresses[sample].city")
            .jsonPath("$.violations[0].message").isEqualTo("size must be between 5 and 15")
    }

    @Test
    internal fun `accept a user fitting the constraints`() {
        val request = """
            {
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
            .isOk
            .expectBody()
            .consumeWith {
                val body= String(it.responseBody!!)
                assertThat(body).isEqualTo("User is valid")
            }
    }
}