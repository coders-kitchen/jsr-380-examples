package com.coderskitchen.jsr380examples

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID
import javax.validation.Valid

@RequestMapping("/orders")
@RestController
@Validated
class OrderController {

    @HasAccessToPriceList
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun submitOrder(@RequestBody order: Order, @RequestHeader("x-user-id") userId: Int): Mono<UUID> = Mono.just(UUID.randomUUID())
}