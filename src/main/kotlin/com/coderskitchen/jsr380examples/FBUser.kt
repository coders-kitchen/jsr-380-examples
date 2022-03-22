package com.coderskitchen.jsr380examples

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class FBUser(
    @get:Size(min=5, max= 30, message = "Name must be between 5 and 30 characters")
    @get:NotNull(message = "Name cannot be null")
    val name: String?,
    @get:SupportedGender(genders = ["male", "female", "cis", "non-binary"])
    val gender: String?
)