package com.coderskitchen.jsr380examples

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException

@RestControllerAdvice
class ErrorAdvice {
    companion object {
        private val log = LoggerFactory.getLogger(ErrorAdvice::class.java)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleValidationExceptions(ex: WebExchangeBindException): String {
        val errors: MutableMap<String, String> = HashMap()

        ex.bindingResult.allErrors.forEach { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage() ?: "empty error message"
            errors[fieldName] = errorMessage
        }
        return errors.map { (k,v) -> "$k -> $v" }.joinToString()
    }
}