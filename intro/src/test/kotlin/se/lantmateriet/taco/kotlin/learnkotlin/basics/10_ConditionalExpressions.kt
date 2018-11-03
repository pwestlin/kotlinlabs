@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConditionalExpressionsTest {

    @Test
    fun `conditional expressions`() {
        // Följande finns inte i Kotlin:
        // condition ? then : else
        // ...men conditional statements returnerar värden!
        val retFromCond = if (2 > 1) 2 else 1

        assertThat(retFromCond).isEqualTo(2)
    }

    @Test
    fun `conditional ternary operator - finns inte`() {
        // Java
        /*
        class Foo {
            private String bar = null;
            public String getBar() {
                return bar;
            }
        }

        Foo foo = new Foo();
        String bar = foo.getBar() != null ? bar : "";   // conditional ternary operator
         */

        class Animal(val name: String? = null)


        val animal = Animal()
        val bar = animal.name ?: ""

        assertThat(bar).isEqualTo("")
    }

}