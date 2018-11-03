@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

class WhenTest {

    @Test
    // Jämför med Javas switch
    fun `when expressions`() {
        val obj: Any = 0.23

        val string =
            when (obj) {
                1 -> "One"
                "Hello" -> "Greeting"
                is Long -> "Long"
                !is String -> "Not a string"
                else -> "Unknown"
            }

        assertThat(string).isEqualTo("Not a string")
    }


    @Test
    fun `when - status`() {
        fun getSomeInternalStatusCode(status: HttpStatus) = when (status) {
            OK -> 1
            BAD_GATEWAY, SERVICE_UNAVAILABLE -> 2
            else -> throw RuntimeException("HttpStatus $status is not ok by me!")
        }

        assertThat(getSomeInternalStatusCode(OK)).isEqualTo(1)
        assertThat(getSomeInternalStatusCode(BAD_GATEWAY)).isEqualTo(2)
        assertThat(getSomeInternalStatusCode(SERVICE_UNAVAILABLE)).isEqualTo(2)

        Assertions.assertThatThrownBy { getSomeInternalStatusCode(BAD_REQUEST) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("HttpStatus 400 BAD_REQUEST is not ok by me!")
    }

}
