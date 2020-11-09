@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package nu.westlin.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

typealias CustomerName = String
typealias Address = String

class TypeAliasesTest {

    data class CustomerWithTypeAliases(val name: CustomerName, val address: Address)

    @Test
    fun `create customer`() {
        val customer = CustomerWithTypeAliases("Kung Bore", "Kallervalla")

        with(customer) {
            assertThat(name).isEqualTo("Kung Bore")
            assertThat(address).isEqualTo("Kallervalla")
        }
    }

    @Test
    fun `typealiases is just ALIASES and not TYPES`() {
        val customerName: CustomerName = "foo"
        val address: Address = "foo"

        assertThat(customerName == address).isTrue

        // A way to use actual types in a less expensive (way compared to create a class) is inline classes: https://kotlinlang.org/docs/reference/inline-classes.html
    }
}