@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package nu.westlin.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ClassesTest {

    @Test
    fun `classes and objects`() {
        // Japp, man kan skapa klasser inuti en funktion
        class Car(val brand: String, var name: String)

        val car = Car("Volvo", "Pärlan")
        assertThat(car.brand).isEqualTo("Volvo")
        assertThat(car.name).isEqualTo("Pärlan")

        //car.brand = "Opel"    // Näru, detta går inte!
        car.name = "Skrothögen"
        assertThat(car.brand).isEqualTo("Volvo")
        assertThat(car.name).isEqualTo("Skrothögen")

        println("car = $car")

        // Kommer ni ihåg "named parameters" för funktioner? De funkar även i konstruktorer
        val varstaGussenPaBussen = Car(brand = "Ford", name = "Röda Faran")
        assertThat(varstaGussenPaBussen.brand).isEqualTo("Ford")
        assertThat(varstaGussenPaBussen.name).isEqualTo("Röda Faran")
    }

    @Test
    fun inheritance() {
        // Klasser är final by default så man måste märka dem med "open" vid arv
        open class Animal(val noLegs: Int) {
            // Metoder är också final by default - och public
            open fun sound() = "Schhh"
        }

        class Fish : Animal(0) {
            override fun sound() = "Blubb"
        }

        class Fox : Animal(4) {
            override fun sound() = "What does twe fox say?"
        }

        val threeLeggedAnimal = Animal(3)
        assertThat(threeLeggedAnimal.noLegs).isEqualTo(3)
        assertThat(threeLeggedAnimal.sound()).isEqualTo("Schhh")

        val fox = Fox()
        assertThat(fox.noLegs).isEqualTo(4)
        assertThat(fox.sound()).isEqualTo("What does twe fox say?")


        //class Salmon : Fish       // Hä gånt nå för Fish är final (by default)


        // Abstrakta klasser är (självklart) inte final och behöver således inte ha open
        abstract class Vehicle
        class Boat : Vehicle()
    }

}