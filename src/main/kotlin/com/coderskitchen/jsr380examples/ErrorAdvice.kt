package com.coderskitchen.jsr380examples

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.zalando.problem.spring.webflux.advice.ProblemHandling

@RestControllerAdvice
class ErrorAdvice : ProblemHandling {
    companion object {
        private val log = LoggerFactory.getLogger(ErrorAdvice::class.java)
    }
}