package nu.westlin.kotlin.learnkotlin.lessbasic

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

private fun <T> retryable(noRetries: Int = 3, block: () -> T): T {

    var retries = 0
    var result: T? = null
    (0 until noRetries).forEach { _ ->
        try {
            result = block()
            return result!!
        } catch (e: RuntimeException) {
            if (retries < noRetries - 1) {
                Thread.sleep(100)
                retries += 1
            } else {
                throw e
            }
        }
    }
    return result!!
}

private class RetryableTest {

    private class Repository {
        fun foo(): String = "foo"
    }

    private val mock = mockk<Repository>()

    @Test
    fun `foo should be called once and return its value`() {
        every { mock.foo() } returns "foobar"

        assertThat((retryable { mock.foo() })).isEqualTo("foobar")

        verify(exactly = 1) { mock.foo() }
    }

    @Test
    fun `foo should be called three times and return its value`() {
        every { mock.foo() } throws RuntimeException("fail 1") andThenThrows RuntimeException("fail2") andThen "foobar"

        assertThat((retryable { mock.foo() })).isEqualTo("foobar")

        verify(exactly = 3) { mock.foo() }
    }

    @Test
    fun `foo should be called three times and throw exception`() {
        every { mock.foo() } throws RuntimeException("fail")

        assertThatThrownBy { retryable { mock.foo() } }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("fail")

        verify(exactly = 3) { mock.foo() }
    }
}