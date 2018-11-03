@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RangesTest {

    @Test
    fun range() {
        assertThat(IntRange(2, 4)).containsExactly(2, 3, 4)
        assertThat(CharRange('b', 'd')).containsExactly('b', 'c', 'd')

        // Kommer inte funka för å, ä, ö ligger
        assertThat(CharRange('å', 'ö')).containsExactly('å', 'ä', 'ö')
    }

    @Test
    fun `shorter range`() {
        assertThat(1..3).containsExactly(1, 2, 3)
    }

    @Test
    fun `in range`() {
        val x = 5
        val y = 7
        if (x in 1..y) {
            println("in range")
        }
    }

    @Test
    fun progressions() {
        // progressions
        for (num in 1..10 step 2) {
            print("$num ")
        }
        println()
        for (num in 9 downTo 0 step 3) {
            print("$num ")
        }

    }
}