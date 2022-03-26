package com.coderskitchen.jsr380examples

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.hibernate.validator.constraints.Range
import org.hibernate.validator.constraints.URL
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    visible = true,
    property = "source",
    defaultImpl = UnknownSource::class)
@JsonSubTypes(value = [
    JsonSubTypes.Type(value = UserRegisteredDirectly::class, name = "direct"),
    JsonSubTypes.Type(value = UserRegisteredViaGoogle::class, name = "google"),
])
interface User {
    @get:Pattern(regexp = "(direct|google)", message = "Only direct or google registration is supported")
    @get:NotNull
    val source: String?
}

class UnknownSource(override val source: String?) : User

data class UserRegisteredDirectly (
    override val source: String,
    @get:Size(min=5, max= 30, message = "Name must be between 5 and 30 characters")
    @get:NotNull(message = "Name cannot be null")
    val name: String?,
    @get:Range(min = 18, max  = 150, message = "Age must be between 18 and 150")
    val age: Int,
    @get:Size(min=0, max=200, message = "About Me must be between 0 and 200 characters")
    val aboutMe: String?,
) : User

data class UserRegisteredViaGoogle (
    override val source: String,
    @get:Size(min=10, max= 120, message = "Name must be between 10 and 120 characters")
    @get:NotNull(message = "Name cannot be null")
    val name: String?,
    @get:URL
    val profileSource: String

) : User