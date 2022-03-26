package com.coderskitchen.jsr380examples

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.reflect.KClass


@Size(min=5, max= 30, message = "Name must be between 5 and 30 characters")
@NotNull(message = "Name cannot be null")
@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
annotation class ValidUser(
    val message: String = "Valid User",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
