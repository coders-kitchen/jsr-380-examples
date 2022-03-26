package com.coderskitchen.jsr380examples

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/users")
@RestController
class UserController {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun addUser(@Valid @RequestBody user: User): Mono<String> = Mono.just("User is valid and registered via ${user.source}").doOnSuccess { println(user) }
}