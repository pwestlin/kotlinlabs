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
            // samma som ovan men it är namnsatt till item
            assertThat(list.last({ item -> item.length == 6 })).isEqualTo("Kotlin")

            // Om den sista parametern til en funktion är ett lambda
            // kan det flyttas ut ur paranteserna
            assertThat(list.last { it.length == 6 }).isEqualTo("Kotlin")
        }

        @Test
        fun `findLast`() {
            assertThat(list.findLast { it.length == 6 }).isEqualTo("Kotlin")
        }

        // filter, map, first, groupBy, let, apply osv osv osv osv osv....
    }

    @Test
    fun `when does a lambda run?`() {
        // TODO petves: Bra exempel på att ett lambda körs "sen" :)

        fun division(namnare: Int, taljare: Int): Int {
            try {
                return taljare / namnare
            } catch (e: Throwable) {
                println("Det gick inte att dela $taljare med $namnare")
                throw e
            }
        }

        division(3, 1)
        division(3, 0)
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


}