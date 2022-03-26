package com.coderskitchen.jsr380examples

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AdultItemsRequireAdultReceiversValidator::class])
annotation class AdultItemsRequireAdultReceivers(
    val message : String = "This order requires an adult receiver",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)


class AdultItemsRequireAdultReceiversValidator : ConstraintValidator<AdultItemsRequireAdultReceivers, Order> {
    override fun isValid(value: Order, context: ConstraintValidatorContext?): Boolean {
        return !(value.containsAdultItems && value.receiver.age < 18)
    }
}
