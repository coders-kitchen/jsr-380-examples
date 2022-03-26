package com.coderskitchen.jsr380examples

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.NotEmpty

data class Order (
    @Range(min = 1, max = 1500)
    val selectedPriceList: Int,
    @NotEmpty
    val items: List<String>
)