package com.coderskitchen.jsr380examples

import org.springframework.stereotype.Component

@Component
class PriceListProvider {

    fun getPriceListsForUser(userId: Int) = listOf(1, 2, 3, 4)
}