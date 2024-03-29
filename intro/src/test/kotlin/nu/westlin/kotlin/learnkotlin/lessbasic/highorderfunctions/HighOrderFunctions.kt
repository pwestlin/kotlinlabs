@file:Suppress("UnnecessaryVariable")

package nu.westlin.kotlin.learnkotlin.lessbasic.highorderfunctions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HighOrderFunctions {

    class MiscTest {
        @Test
        fun `apply String functions to Int`() {
            fun functionOnInt(int: Int, function: Int.() -> String) = int.function()

            assertThat(functionOnInt(25) { toString() }).isEqualTo("25")
            assertThat(functionOnInt(25) { toString().reversed() }).isEqualTo("52")
            assertThat(functionOnInt(25) { toString().reversed().plus(3) }).isEqualTo("523")
        }
    }

    class FoldAndFilterTest {
        private val intItems = 1..5

        @Test
        fun `sum ints using fold`() {
            assertThat(intItems.fold(0) { acc, e -> acc + e }).isEqualTo(15)
        }

        @Test
        fun `sum ints using rightFold`() {
            val items = listOf(1, 2, 3, 4, 5)
            assertThat(items.foldRight(1) { e, acc -> acc + e }).isEqualTo(16)
        }

        @Test
        fun `filter even numbers using implicit return`() {
            assertThat(intItems.filter {
                it % 2 == 0
            }).containsExactly(2, 4)
        }

        @Test
        fun `filter even numbers using like half implicit return`() {
            assertThat(intItems.filter {
                val even = it % 2 == 0
                even
            }).containsExactly(2, 4)
        }

        @Test
        fun `filter even numbers using explicit return`() {
            assertThat(intItems.filter {
                val even = it % 2 == 0
                return@filter even
            }).containsExactly(2, 4)
        }
    }

    class MathOperationsTest {
        // x = first Int, y = second Int
        // operation = mathematical operation(function) to to on x and y
        private fun mathOperation(x: Int, y: Int, operation: (Int, Int) -> Int) = operation(x, y)

        private val addOperation = { x: Int, y: Int -> x + y }
        private val subtractOperation = { x: Int, y: Int -> x - y }

        @Test
        fun `math operation function`() {

            assertThat(mathOperation(3, 5, addOperation)).isEqualTo(8)
            assertThat(mathOperation(3, 5, subtractOperation)).isEqualTo(-2)
        }

        @Test
        fun `invoke a function type instance`() {
            assertThat(addOperation.invoke(7, 3)).isEqualTo(10)
            assertThat(addOperation(7, 3)).isEqualTo(10)
        }
    }
}