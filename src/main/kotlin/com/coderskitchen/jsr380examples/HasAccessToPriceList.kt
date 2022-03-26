package com.coderskitchen.jsr380examples

import org.springframework.beans.factory.annotation.Autowired
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraintvalidation.SupportedValidationTarget
import javax.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [HasAccessToPriceListValidator::class])
annotation class HasAccessToPriceList(
    val message: String = "You can not use the selected price list",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
class HasAccessToPriceListValidator : ConstraintValidator<HasAccessToPriceList, Array<Any>>{

    @Autowired
    lateinit var priceListProvider: PriceListProvider

    override fun isValid(value: Array<Any>, context: ConstraintValidatorContext): Boolean {
        if(value.size != 2)
            return true

        if(value[0] !is Order)
            return true

        if(value[1] !is Int)
            return true

        val order = value[0] as Order
        val userId = value[1] as Int

        val isValid = order.selectedPriceList in priceListProvider.getPriceListsForUser(userId)
        if(!isValid) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("Chosen price list id ${order.selectedPriceList} is not available to you")
                .addPropertyNode("selectedPriceList")
                .addConstraintViolation()
        }
        return isValid
    }
}
