@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName", "CanSealedSubClassBeObject", "UsePropertyAccessSyntax")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


/*
Sealed classes är "hybrider" mellan klasser och enums.
En subklass till en sealed klass måste ligga i samma Kotlin-fil som basklassen.

Vad ska man då med dessa till?
Tänk Verksamhetsatgard i domän Fastighet. Den får bara ha tre implementationer (TekniskAtgard, OvrigRegisteratgard och FastighetsrattsligAtgard)
men hur ska man förhindra det på ett enkelt sätt i Java?
I Kotlin skulle man göra Verksamhetsatgard till en sealed class och sedan lägga dess impl. (TekniskAtgard, OvrigRegisteratgard och FastighetsrattsligAtgard)
i samma Kotlin-fil och sedan var det bra med det.
 */

sealed class OffspringOfPeters(val name: String, val age: Int)
class Adam : OffspringOfPeters("Adam", 14)
class Felix : OffspringOfPeters("Felix", 11)

sealed class Car {
    object Volvo : Car()
    object Saab : Car()

    companion object {
        fun fromString(string: String): Car {
            return Car::class.sealedSubclasses
                .firstOrNull {
                    it.objectInstance!!::class.simpleName == string
                }
                ?.objectInstance
                ?: throw RuntimeException("Can't find car $string")
        }
    }
}

class SealedClassesTest {

    @Test
    fun `sealed classes`() {
        fun isTonaring(offspring: OffspringOfPeters): Boolean {
            return when (offspring) {
                is Adam -> true
                is Felix -> false
                // Behövs inte - wohoo!
                //else -> throw RuntimeException("Typ $offspring stöds inte")
            }
        }

        assertThat(isTonaring(Felix())).isFalse()
        assertThat(isTonaring(Adam())).isTrue()
    }

    @Test
    fun `sealed classes - cars`() {

        fun gettingUpdates(car: Car) = when (car) {
            is Car.Volvo -> true
            is Car.Saab -> false
        }

        assertThat(gettingUpdates(Car.Volvo)).isTrue()
        assertThat(gettingUpdates(Car.Saab)).isFalse()

    }

    @Test
    fun `iterate over sealed class`() {
        assertThat(Car.fromString("Volvo")).isEqualTo(Car.Volvo)
    }
}

sealed class Fruit(open val isGood: Boolean) {
    data class Apple(override val isGood: Boolean) : Fruit(isGood)
    object Pineapple : Fruit(false)
}

class SealedDataClassesTest {

    @Test
    fun `a good apple`() {
        assertThat(Fruit.Apple(true).isGood).isTrue()
    }

    @Test
    fun `a bad apple`() {
        assertThat(Fruit.Apple(false).isGood).isTrue()
    }

    @Test
    fun `pineapple is never good`() {
        assertThat(Fruit.Pineapple.isGood).isFalse()
    }
}

sealed class ApplicationError(open val message: String?) {

    object LoginError : ApplicationError(null)
    data class PostFormError(override val message: String) : ApplicationError(message)
}

class ApplicationErrorTest {

    @Test
    fun `login error`() {
        assertThat(ApplicationError.LoginError.message).isNull()
    }

    @Test
    fun `post form error`() {
        assertThat(ApplicationError.PostFormError("You prvided bad data").message).isEqualTo("You prvided bad data")
    }
}
