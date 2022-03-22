package com.coderskitchen.jsr380examples

import javax.validation.constraints.Size

data class Address (@get:Size(min=5, max= 30)
                     val street: String,
                    @get:Size(min=5, max= 15)
                     val city: String)