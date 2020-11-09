@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName", "NON_EXHAUSTIVE_WHEN", "UNUSED_EXPRESSION")

package nu.westlin.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_GATEWAY
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import java.io.File

class WhenTest {

    @Suppress("MoveVariableDeclarationIntoWhen")
    @Test
    // Jämför med Javas switch
    fun `when expressions`() {
        val obj: Any = 0.23

        val string =
            when (obj) {
                1 -> "One"
                "Hello" -> "Greeting"
                is Long -> "Long"       // is => Javas cast: (Fastighet) utbytesobjekt
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

    @Test
    fun `when in range`() {
        fun inRange(int: Int) = when (int) {
            in 0..9 -> "0..9"   // in is syntactic sugar for the contains function
            in 10..99 -> "10..99"
            else -> "too big"
        }

        assertThat(inRange(5)).isEqualTo("0..9")
        assertThat(inRange(99)).isEqualTo("10..99")
        assertThat(inRange(100)).isEqualTo("too big")
    }

    @Test
    fun `smart casts`() {
        open class Anything
        class Thing(val foo: String): Anything()
        class Gadget(val bar: String): Anything()

        fun getAnythingAsString(anything: Anything) = when(anything) {
            is Thing ->  anything.foo  // No explicit cast need, Kotlin has already casted Something to Thing
            is Gadget -> anything.bar  // No explicit cast need, Kotlin has already casted Something to Gadget
            else -> "unknown"
        }

        assertThat(getAnythingAsString(Thing("Get up an milk the cow slacker!"))).isEqualTo("Get up an milk the cow slacker!")
    }

    @Test
    fun `when without parameter`() {
        val string = "fågelholk"

        // When turns out to be like if else-if else
        val isFooOrBar = when {
            string == "foo" -> true
            string == "bar" -> true
            else -> false
        }
        assertThat(isFooOrBar).isFalse

        // ...or a better form: "when with subject"
        val isFooOrBar2 = when (string) {
            "foo" -> true
            "bar" -> true
            else -> false
        }
        assertThat(isFooOrBar2).isFalse
    }

    @Test
    fun `when can be used an all types, not just constants and enums (or Strings from Java 7) which is the limitation in Java`() {
        val path = File("/tmp")

        val type = when {
            path.isDirectory -> "directory"
            path.isFile -> "file"
            else -> "What in the world is this?"
        }

        // Better than if else-if else? In this case - I think it is!

        assertThat(type).isEqualTo("directory")
    }

    // When and exhaustive: https://proandroiddev.com/til-when-is-when-exhaustive-31d69f630a8b
    private data class Result(val type: Type) {
        enum class Type {
            SUCCESS, ERROR
        }
    }

    @Test
    fun `when is exhaustive when used as an expression`() {
        val feeling = when (Result(Result.Type.ERROR).type) {
            Result.Type.SUCCESS -> "Wohoo!"
            Result.Type.ERROR -> "Booh!"    // Compiler error if branch not added
        }

        assertThat(feeling).isEqualTo("Booh!")
    }

    @Test
    fun `when is NOT exhaustive when NOT used as an expression`() {
        when (Result(Result.Type.ERROR).type) {
            Result.Type.SUCCESS -> "Wohoo!"
            //Result.Type.ERROR -> "Booh!"  // No compiler error
        }
    }

    // Extension proerty for ALL classes
    val <T> T.exhaustive: T
        get() = this

    @Test
    fun `when is exhaustive when we use exhaustive`() {
        when (Result(Result.Type.ERROR).type) {
            Result.Type.SUCCESS -> "Wohoo!"
            Result.Type.ERROR -> "Booh!"  // Compiler error if branch not added
        }.exhaustive
    }

    @Test
    fun `when is exhaustive when we use exhaustive even on String`() {
        when ("foo") {
            "foo" -> "Wohoo!"
            "bar" -> "Booh!"
            else -> "Say what?" // Compiler error if branch not added
        }.exhaustive
    }
}

