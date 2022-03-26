package com.coderskitchen.jsr380examples

data class User(
    @get:MatchesNamingConvention
    val name: String?,
)