package nu.westlin.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class NonEmptyListWithRuntimeCheckTest {

    class NonEmptyList<E> private constructor(list: List<E>) : List<E> by list {

        override fun isEmpty(): Boolean = false

        companion object {
            fun <T> of(vararg elements: T): NonEmptyList<T> {
                // Runtime check
                require(elements.isNotEmpty()) { "elements must be at least of size 1" }
                return NonEmptyList(listOf(*elements))
            }
        }
    }

    @Test
    fun `no value`() {
        Assertions.assertThatThrownBy { NonEmptyList.of<Int>() }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("elements must be at least of size 1")
    }

    @Test
    fun `one value`() {
        val of = NonEmptyList.of(1)
        Assertions.assertThat(of).containsExactly(1)
    }

    @Test
    fun `more than one values`() {
        val of = NonEmptyList.of(1, 2)
        Assertions.assertThat(of).containsExactly(1, 2)
    }
}

class NonEmptyListWithCompileTimeCheckTest {

    class NonEmptyList<E> private constructor(list: List<E>) : List<E> by list {
        companion object {
            fun <T> of(head: T, vararg elements: T): NonEmptyList<T> {
                return NonEmptyList(listOf(head) + listOf(*elements))
            }
        }

        override fun isEmpty(): Boolean = false
    }

    @Test
    fun `no value`() {
        // Compile error
        // NonEmptyList.of<Int>()
    }

    @Test
    fun `one value`() {
        val of = NonEmptyList.of(1)
        Assertions.assertThat(of).containsExactly(1)
    }

    @Test
    fun `more than one values`() {
        val of = NonEmptyList.of(1, 2)
        Assertions.assertThat(of).containsExactly(1, 2)
    }
}