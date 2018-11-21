package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

open class Result<T>
class Success<T> : Result<T>()
class Failure<T>(val errorMessage: String, val exception: java.lang.Exception) : Result<T>()

fun <T : Any> safeApiCall(call: () -> Result<T>, errorMessage: String): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        Failure(errorMessage, e)
    }
}

class SafeApiCallTest {

    class Api {
        fun <T : Any> getResult(value: String): Result<T> {
            return when (value) {
                "foo", "bar" -> Success()
                else -> throw RuntimeException("$value could not be handled")
            }
        }
    }

    @Test
    fun `call a non-safe API-function in a safe way`() {
        var result = safeApiCall({ Api().getResult<String>("foo") }, "Could not do it")
        assertThat(result).isInstanceOf(Success::class.java)

        result = safeApiCall({ Api().getResult<String>("foobar") }, "Could not do it")
        assertThat(result).isInstanceOf(Failure::class.java)
        with(result as Failure) {
            assertThat(errorMessage).isEqualTo("Could not do it")
            assertThat(exception).isInstanceOf(RuntimeException::class.java)
        }
    }
}