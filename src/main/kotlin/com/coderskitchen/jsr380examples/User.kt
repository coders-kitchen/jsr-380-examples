package com.coderskitchen.jsr380examples

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Range

data class User(
    @get:Size(min=5, max= 30, message = "Name must be between 5 and 30 characters")
    @get:NotNull(message = "Name cannot be null")
    val name: String?,
    @get:Range(min = 18, max  = 150, message = "Age must be between 18 and 150")
    val age: Int,
    @get:Size(min=10, max=200, message = "About Me must be between 10 and 200 characters")
    val aboutMe: String
)