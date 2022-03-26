package com.coderskitchen.jsr380examples

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Size

@AdultItemsRequireAdultReceivers
data class Order (
    val containsAdultItems: Boolean,
    val receiver: Receiver
)

data class Receiver(@get:Range(min = 15, max  = 150, message = "Age must be between {min} and {max}")
                    val age: Int,
                    @get:Size(min = 5, max = 100, message = "Name must be between {min} and {max} characters")
                    val name: String)