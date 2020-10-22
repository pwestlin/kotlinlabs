package nu.westlin.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class Casts {

    @Test
    fun `as is an ass`() {
        val anyNotNullVal: Any = "foo"
        assertThat(anyNotNullVal as String).isEqualTo("foo")

        val anyNullableVal1: Any? = "foo"
        assertThat(anyNullableVal1 as String).isEqualTo("foo")

        val anyNullableVal2: Any? = null
        assertThatThrownBy { anyNullableVal2 as String }
            .isInstanceOf(NullPointerException::class.java)
            .hasMessage("null cannot be cast to non-null type kotlin.String")
        assertThat(anyNullableVal2 as? String).isNull()
        assertThat(anyNullableVal2 as String?).isNull()
    }
}