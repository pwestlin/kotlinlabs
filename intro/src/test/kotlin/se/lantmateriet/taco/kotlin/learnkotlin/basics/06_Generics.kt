@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GenericsTest {


    class Stack<E>(vararg items: E) {
        private val elements = items.toMutableList()

        fun push(item: E) {
            elements.add(item)
        }

        fun pop() = elements.removeAt(elements.size - 1)
        fun peek(): E = elements.last()
    }

    @Test
    fun generics() {
        val stack = Stack("foo", 4)
        assertThat(stack.peek()).isEqualTo(4)
        stack.push(5.67)
        assertThat(stack.peek()).isEqualTo(5.67)
        assertThat(stack.pop()).isEqualTo(5.67)
        assertThat(stack.pop()).isEqualTo(4)
        assertThat(stack.peek()).isEqualTo("foo")

        val intStack = Stack(1, 6, 3)
        assertThat(intStack.peek()).isEqualTo(3)
        //intStack.push("foo")      // Går inte eftersom stacken automatiskt typats till int
    }

    @Test
    fun `generic functions`() {
        fun <E> mutableStackOf(vararg elements: E) = Stack(*elements)

        val genericStack = mutableStackOf("a", 1)
        genericStack.push(12.32)

        val intStack = mutableStackOf(1, 2, 3)    // Går inte för "a" och 2.34 är inte Int
        // intStack.push("foo")      // Går inte för typen har automatiskt blivit Int
        // val intStack = mutableStackOf<Int>("a", 1, 2.34)    // Går inte för "a" och 2.34 är inte Int
    }

}
