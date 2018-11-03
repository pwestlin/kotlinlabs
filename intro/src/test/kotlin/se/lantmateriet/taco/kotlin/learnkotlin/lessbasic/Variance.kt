@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.junit.Test

class VarianceTest {

    @Test
    fun `invariant`() {
        val elements : MutableList<Any>
        val strings = mutableListOf("a", "bc")

        //elements = strings    // Går inte för elements är mutable<Any>, dvs "jag kan stoppa in precis vad som helst, inte bara strängar"
    }

    @Test
    fun `covariant`() {
        val elements : List<Any>
        val strings = mutableListOf("a", "bc")

        elements = strings  // Detta funkar eftersom som elements är immutable, dvs jag kan inte uppdatera listan
    }

    @Test
    fun `in`() {
        class Taker<in T> {
            fun takes(t: T) {}
        }
    }

    @Test
    fun `out`() {
        class Giver<out T> {
            //fun gives(): T {}
        }
    }
}