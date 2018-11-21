package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

open class Result
class Success : Result()
class Failure(val errorMessage: String, val exception: java.lang.Exception) : Result()

fun safeApiCall(call: () -> Result, errorMessage: String): Result {
    return try {
        call()
    } catch (e: Exception) {
        Failure(errorMessage, e)
    }
}

class SafeApiCallTest {

    class Api {
        fun getResult(value: String): Result {
            return when (value) {
                "foo", "bar" -> Success()
                else -> throw RuntimeException("$value could not be handled")
            }
        }
    }

    @Test
    fun `call a non-safe API-function in a safe way`() {
        var result = safeApiCall({ Api().getResult("foo") }, "Could not do it")
        assertThat(result).isInstanceOf(Success::class.java)

        result = safeApiCall({ Api().getResult("foobar") }, "Could not do it")
        assertThat(result).isInstanceOf(Failure::class.java)
        with(result as Failure) {
            assertThat(result.errorMessage).isEqualTo("Could not do it")
            assertThat(result.exception).isInstanceOf(RuntimeException::class.java)
        }
    }
}