package com.coderskitchen.jsr380examples

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MatchesNamingConventionValidator::class])
annotation class MatchesNamingConvention(
    val message: String = "\${validatedValue} does not match the naming conventions (^[A-Z].*)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class MatchesNamingConventionValidator : ConstraintValidator<MatchesNamingConvention, String> {
    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        context.unwrap(HibernateConstraintValidatorContext::class.java)
            .buildConstraintViolationWithTemplate("'$value' does not match the naming conventions (^[A-Z].*)")
            .enableExpressionLanguage(ExpressionLanguageFeatureLevel.BEAN_METHODS)
            .addConstraintViolation()
            .disableDefaultConstraintViolation()
        return value.matches(Regex("^[A-Z].*"))
    }

}
