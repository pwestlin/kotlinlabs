package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class RunCatching {

    private class Service {
        fun doSomething(something: String?): String {
            return something?.let { something } ?: throw RuntimeException("something can't be null!")
        }
    }

    private val service = Service()

    @Test
    fun `doSomething nice`() {
        with("nice stuff") {
            assertThat(service.doSomething(this)).isEqualTo(this)
        }
    }

    @Test
    fun `doSomething not so nice nice`() {
        assertThatThrownBy { service.doSomething(null) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("something can't be null!")
    }

    @Test
    fun `doSomething nice with runCatching`() {
        val result = runCatching { service.doSomething("nice stuff") }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("nice stuff")
        assertThat(result.exceptionOrNull()).isNull()
    }

    @Test
    fun `doSomething not so nice with runCatching`() {
        val result = runCatching { service.doSomething(null) }
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        assertThat(result.exceptionOrNull()).hasMessage("something can't be null!")
        assertThat(result.getOrNull()).isNull()
    }
}