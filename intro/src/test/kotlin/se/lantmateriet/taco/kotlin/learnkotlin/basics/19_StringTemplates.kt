@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StringTemplatesTest {

    @Test
    fun `properties`() {
        val name = "Peter"
        val age = 46

        assertThat("$name är $age år gam...ung").isEqualTo("Peter är 46 år gam...ung")
    }

    @Test
    fun `properties from object`() {
        class Character(val name: String, val age: Int)

        val pingu = Character("Pingu", 32)

        assertThat("${pingu.name} är ${pingu.age} år gammal").isEqualTo("Pingu är 32 år gammal")
    }
}