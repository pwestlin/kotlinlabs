@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package nu.westlin.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EqualityTest {

    // == är samma som equals i Java
    // === är "samma objektreferens"
    @Test
    fun `equality Checks`() {
        // Med data classes får man automatiskt equals/hashCode
        data class Car(val brand: String, val model: String, val year: String)

        val car = Car("Volvo", "PV", "1970")

        val equalCar = Car("Volvo", "PV", "1970")
        assertThat(equalCar == car).isTrue
        assertThat(equalCar === car).isFalse

        val notEqualCar = Car("Volvo", "Amazon", "1966")
        assertThat(notEqualCar == car).isFalse
        assertThat(notEqualCar === car).isFalse

        val sameCar = car
        assertThat(sameCar == car).isTrue
        assertThat(sameCar === car).isTrue


        val isNull = null
        val isNotNull = "foo"
        assertThat(isNull == isNotNull).isFalse   // == funkar alltså på null utan att ge NPE :)
    }


}