package nu.westlin.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

@Suppress("UNUSED_VARIABLE")
class CheckTest {

    @Test
    fun `check checks state`() {
        val nullable: String? = "not null"
        check(nullable != null) { "nullable must not be null" }

        val nullable2: String? = "not null"
        checkNotNull(nullable2) { "nullable2 must not be null" }

        // ..or even better, checkNotNull returns the notNullValue
        val notNullable: String = checkNotNull("not null") { "string must not be null" }

        assertThatThrownBy {
            @Suppress("SENSELESS_COMPARISON")
            check(null != null) { "value must not be null" }
        }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("value must not be null")

        assertThatThrownBy {
            @Suppress("SENSELESS_COMPARISON", "IMPLICIT_NOTHING_AS_TYPE_PARAMETER")
            checkNotNull (null) { "value must not be null" }
        }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("value must not be null")
    }

    @Test
    fun `require checks arguments`() {
        fun function(value: Int) {
            require(value >= 0) { "$value must be >= 0"}
        }

        assertThat(function(0)).isInstanceOf(Unit::class.java)
        assertThat(function(1)).isInstanceOf(Unit::class.java)

        assertThatThrownBy {
            function(-1)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("-1 must be >= 0")

        fun intToString(value: Any): String? = when(value) {
            is Int -> value.toString()
            else -> null
        }

        require(intToString(5) == "5")
        val string = requireNotNull(intToString(5))
        assertThat(string).isEqualTo("5")

        assertThatThrownBy {
            requireNotNull(intToString("a")) { "value is null" }
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("value is null")
    }
}