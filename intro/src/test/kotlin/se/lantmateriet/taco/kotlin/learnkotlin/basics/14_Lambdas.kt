@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LambdasTest {

    class LambdasStandardLibrary {
        private val list = listOf("Hello", "from", "the", "wonderful", "world", "of", "Kotlin", "programming")

        @Test
        fun `last`() {
            assertThat(list.last()).isEqualTo("programming")

            // it?
            assertThat(list.last({ it.length == 6 })).isEqualTo("Kotlin")
            // samma som ovan men it är namnsatt till string
            assertThat(list.last({ string -> string.length == 6 })).isEqualTo("Kotlin")

            // Om den sista parametern til en funktion är ett lambda
            // kan (och ska enligt god sed) den flyttas ut ur paranteserna
            assertThat(list.last { it.length == 6 }).isEqualTo("Kotlin")
        }

        @Test
        fun `findLast`() {
            assertThat(list.findLast { it.length == 6 }).isEqualTo("Kotlin")
        }

        // filter, map, first, groupBy, let, apply osv osv osv osv osv....
    }

    @Test
    fun `lambda compute`() {
        val add = { a: Int, b: Int -> a + b }
        val subtract = { a: Int, b: Int -> a - b }
        val multiply = { a: Int, b: Int -> a * b }

        fun compute(a: Int, b: Int, function: (Int, Int) -> Int): Int {
            return function(a, b)
        }

        assertThat(compute(4, 2, add)).isEqualTo(6)
        assertThat(compute(4, 2, subtract)).isEqualTo(2)
        assertThat(compute(4, 2, multiply)).isEqualTo(8)
        assertThat(compute(4, 2, { a: Int, b: Int -> a / b })).isEqualTo(2)
        assertThat(compute(4, 2) { a: Int, b: Int -> a / b }).isEqualTo(2)
    }


    @Suppress("DIVISION_BY_ZERO")
    @Test
    fun `when does a lambda run?`() {
        fun <R> runLambda(function: () -> R) = function()

        val function = { 1 / 0 }
        println(runLambda(function))
    }

    @Suppress("SimplifyBooleanWithConstants")
    @Test
    fun `do something`() {
        fun doSomethingIfSomething(someCondition: () -> Boolean, doSomething: () -> Unit) {
            if (someCondition()) doSomething()
        }

        doSomethingIfSomething({ 4 / 2 == 1 }, { println("Doing something!") })
        doSomethingIfSomething({ 4 / 2 == 2 }, { println("Doing something!") })
    }

    @Suppress("SimplifyBooleanWithConstants")
    @Test
    fun `do something if else`() {
        fun <T> doSomethingIfSomethingOrElseSomethingElse(someCondition: () -> Boolean, doSomething: () -> T, doSomethingElse: () -> T) : T {
            return if (someCondition()) doSomething() else doSomethingElse()
        }

        println(doSomethingIfSomethingOrElseSomethingElse({ 4 / 2 == 1 }, { "Correct!" }, {"Uncorrect"}))
        println(doSomethingIfSomethingOrElseSomethingElse({ 4 / 2 == 2 }, { "Correct!" }, {"Uncorrect"}))
    }

}