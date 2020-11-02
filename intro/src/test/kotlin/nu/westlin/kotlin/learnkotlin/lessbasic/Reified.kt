@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package nu.westlin.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.reflect.KClass

class ReifiedTest {

    fun constructorsOfNotReified(clazz: KClass<*>) = clazz.constructors
    inline fun <reified T> constructorsOf() = T::class.constructors

    @Test
    fun `non reified`() {
        println(constructorsOfNotReified(StringBuilder::class).joinToString("\n"))
    }

    @Test
    fun `reified`() {
        println(constructorsOf<StringBuilder>().joinToString("\n"))
    }

    @Test
    fun `should have smae constructors`() {
        assertThat(constructorsOf<StringBuilder>().joinToString(","))
            .isEqualTo(constructorsOfNotReified(StringBuilder::class).joinToString(","))
    }
}