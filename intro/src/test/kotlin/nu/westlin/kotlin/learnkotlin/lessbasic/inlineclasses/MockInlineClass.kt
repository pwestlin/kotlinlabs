package nu.westlin.kotlin.learnkotlin.lessbasic.inlineclasses

import io.mockk.every
import io.mockk.mockk
import nu.westlin.kotlin.learnkotlin.basics.anyValue
import nu.westlin.kotlin.learnkotlin.basics.value
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@JvmInline
value class InlinedClass(val bar: String)

class InlineFoo {
    fun takesInlinedClassAsAParam(inlinedClass: InlinedClass) = inlinedClass.bar

    fun returnslinedClassAsAParam() = InlinedClass("fossing")
}

internal class InlineClassTest {
    private val inlineFoo = mockk<InlineFoo>()

    @Test
    fun `as a param`() {
        every { inlineFoo.takesInlinedClassAsAParam(anyValue()) } returns "fisk"

        assertThat(inlineFoo.takesInlinedClassAsAParam(InlinedClass("bla"))).isEqualTo("fisk")
    }

    @Test
    fun `as result`() {
        val inlinedClass = InlinedClass("snorre")
        every { inlineFoo.returnslinedClassAsAParam() } returns value(inlinedClass)

        assertThat(inlineFoo.returnslinedClassAsAParam()).isEqualTo(inlinedClass)
    }
}

