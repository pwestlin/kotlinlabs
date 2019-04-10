package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

sealed class CoolResult<T> {
    class Success<T>(val result: T) : CoolResult<T>()
    class Failure<T>(val exception: RuntimeException) : CoolResult<T>()
}

class CoolResultTest {

    private fun success() : CoolResult<String> = CoolResult.Success("foo")
    private fun failure() : CoolResult<String> = CoolResult.Failure(RuntimeException("aaaargh"))

    private fun doStuff(result: CoolResult<String>) : String {
        return when(result) {
            is CoolResult.Success -> result.result
            is CoolResult.Failure -> result.exception.localizedMessage
        }
    }

    @Test
    fun `test success`() {
        assertThat(doStuff(success())).isEqualTo("foo")
    }

    @Test
    fun `test failure`() {
        assertThat(doStuff(failure())).isEqualTo("aaaargh")
    }
}