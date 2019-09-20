package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.Result as Result

class KotlinResultTest {

    class Api {
        fun getResult(value: String): Result<String> {
            return when (value) {
                "foo", "bar" -> Result.success("Could handle $value")
                else -> Result.failure(RuntimeException("$value could not be handled"))
            }
        }
    }

    @Test
    fun `happy path`() {
        val result = Api().getResult("foo")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("Could handle foo")
    }

    @Test
    fun `unhappy path`() {
        val result = Api().getResult("foobar")
        assertThat(result.isFailure).isTrue()
        with(result.exceptionOrNull()) {
            assertThat(this).isInstanceOf(RuntimeException::class.java)
            assertThat(this?.message).isEqualTo("foobar could not be handled")
        }
    }

    @Test
    fun `path decision`() {
        var string = ""
        Api().getResult("foobar")
            .onSuccess { string = "Wohoo!" }
            .onFailure { string = "Bohoo!" }

        assertThat(string).isEqualTo("Bohoo!")
    }

    @Test
    fun `path decision with fold`() {
        val string = Api().getResult("foo")
            .fold({ "Wohoo!" }, { "Bohoo!" })

        assertThat(string).isEqualTo("Wohoo!")
    }
}