package com.coderskitchen.jsr380examples

import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(allowedTargets = [AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER])
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [GenderValidator::class])
@Documented
annotation class SupportedGender(
    val message: String = "Must be one of {genders}",

    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],

    val genders: Array<String>
)

class GenderValidator : ConstraintValidator<SupportedGender, String?> {

    private var genders: Array<String> = emptyArray()

    override fun initialize(annotation: SupportedGender) {
        genders = annotation.genders
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value in genders
    }

}
