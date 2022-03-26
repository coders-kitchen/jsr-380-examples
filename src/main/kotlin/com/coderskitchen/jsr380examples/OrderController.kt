package com.coderskitchen.jsr380examples

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping("/orders")
class OrderController {
    @PostMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun placeOrder(@Valid @RequestBody order: Order): Mono<String> = Mono.empty()
}