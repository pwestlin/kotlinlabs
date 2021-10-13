package nu.westlin.kotlin.learnkotlin.lessbasic.valueclasses

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@JvmInline
value class ValueClass(val bar: String)

class Foo {
    fun takesValueClassAsAParam(valueClass: ValueClass): String = valueClass.bar

    fun returnsValueClass(): ValueClass = ValueClass("fossing")
}

internal class ValueClassTest {
    private val foo = mockk<Foo>()

    @Test
    fun `as a param`() {
        every { foo.takesValueClassAsAParam(anyValueClass()) } returns "fisk"

        assertThat(foo.takesValueClassAsAParam(ValueClass("bla"))).isEqualTo("fisk")
    }

    // TODO petves: Make this work...somehow :)
/*
    @Test
    fun `as a param - matches object`() {
        val valueClass = ValueClass("bla")
        every { foo.takesValueClassAsAParam(inlineValue(valueClass)) } returns "fisk"

        assertThat(foo.takesValueClassAsAParam(valueClass)).isEqualTo("fisk")
    }
*/

    @Test
    fun `as result`() {
        val valueClass = ValueClass("snorre")
        every { foo.returnsValueClass() } returns returnValueClass(valueClass)

        assertThat(foo.returnsValueClass()).isEqualTo(valueClass)
    }
}

