package com.coderskitchen.jsr380examples

import jakarta.validation.Validation
import jakarta.validation.Validator


fun main() {
    val factory = Validation.buildDefaultValidatorFactory()
    val validator: Validator = factory.validator


    validateUser(validator, User(name = null, age = 1, aboutMe = "empty"))
    validateUser(validator, User(name = "Peter", age = 41, aboutMe = "I'm the presenter today"))
}

private fun validateUser(validator: Validator, user: User) {
    println("Validating $user")
    val violations = validator.validate(user)
    for (violation in violations) {
        println(violation.message)
    }
    println("....")
}