@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import kotlin.properties.Delegates

class DelegatedPropertiesTest {

    class House {
        var color: String by Delegates.observable("unknown") { _, old, new ->
            println("The color of the house has changed from $old to $new")
        }
        var name: String by Delegates.vetoable("") { _, old, new ->
            new != old && new.length > 3
        }
        var address: String by Delegates.notNull()
        val roof: Boolean by lazy {
            true
        }
    }

    @Test
    fun `change color of house`() {
        val house = House()
        assertThat(house.color).isEqualTo("unknown")
        house.color = "Red"
        house.color = "Yellow"
    }

    @Test
    fun `change name of house`() {
        val house = House()
        assertThat(house.name).isEqualTo("")
        house.name = "Cottage"
        assertThat(house.name).isEqualTo("Cottage")
        house.name = "Foo"
        assertThat(house.name).isEqualTo("Cottage")
        house.name = "Castle"
        assertThat(house.name).isEqualTo("Castle")
    }

    @Test
    fun `change address of house`() {
        val house = House()

        assertThatThrownBy { house.address }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Property address should be initialized before get.")

        house.address = "Lantmäterigatan, Gävle"
        assertThat(house.address).isEqualTo("Lantmäterigatan, Gävle")
    }

    @Test
    fun `house has roof`() {
        assertThat(House().roof).isTrue()
    }
}