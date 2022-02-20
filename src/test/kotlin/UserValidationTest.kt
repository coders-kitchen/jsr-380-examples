import com.coderskitchen.jsr380examples.User
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserValidationTest {

    lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        val factory = Validation.buildDefaultValidatorFactory()
         validator = factory.validator
    }

    @Test
    internal fun `a user must have a name`() {
        val userWithoutName  = User(null, 20, "t".repeat(20))

        val result = validator.validate(userWithoutName)
        assertThat(result.size).isEqualTo(1)
        assertThat(result.iterator().next().message).isEqualTo("Name cannot be null")
    }

    @ParameterizedTest
    @ValueSource(ints = [4, 31])
    internal fun `a user provided a name fitting the boundaries`(nameLength: Int) {
        val userWithoutName  = User("a".repeat(nameLength), 18 , "t".repeat(20))
        val result = validator.validate(userWithoutName)
        assertThat(result.size).isEqualTo(1)
        assertThat(result.iterator().next().message).isEqualTo("Name must be between 5 and 30 characters")
    }

    @ParameterizedTest
    @ValueSource(ints = [17, 151])
    internal fun `a user is in the age range`(ageOutOfRange: Int) {
        val userWithoutName  = User("Tester", ageOutOfRange , "t".repeat(20))
        val result = validator.validate(userWithoutName)
        assertThat(result.size).isEqualTo(1)
        assertThat(result.iterator().next().message).isEqualTo("Age must be between 18 and 150")
    }

    @ParameterizedTest
    @ValueSource(ints = [9, 201])
    internal fun `a user provided a acceptable about me`(aboutMeLength: Int) {
        val userWithoutName  = User("Tester", 18 , "t".repeat(aboutMeLength))
        val result = validator.validate(userWithoutName)
        assertThat(result.size).isEqualTo(1)
        assertThat(result.iterator().next().message).isEqualTo("About Me must be between 10 and 200 characters")
    }

    @Test
    internal fun `a valid user is accepted`() {
        val userWithoutName  = User("Tester", 20, "t".repeat(20))

        val result = validator.validate(userWithoutName)
        assertThat(result.size).isEqualTo(0)
    }
}