package com.coderskitchen.jsr380examples

import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target( allowedTargets = [AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER]
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [GenderValidator::class])
@Documented
annotation class SupportedGender (
    val message: String = "Must be one of male, female, various",

    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
        )

class GenderValidator : ConstraintValidator<SupportedGender, String?> {
    companion object {
        private val GENDERS = listOf("male", "female", "various")
    }
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value in GENDERS
    }

}
