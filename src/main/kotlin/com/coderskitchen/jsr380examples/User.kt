package com.coderskitchen.jsr380examples

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class User(
    @NotNull(message = "Name cannot be null")
    val name: String,
    @Min(value = 18, message = "Age should not be less than 18")
    @Max(value = 150, message = "Age should not be greater than 150")
    val age: Int,
    @Size(min=10, max=200, message = "About Me must be between 10 and 200 characters")
    val aboutMe: String
)