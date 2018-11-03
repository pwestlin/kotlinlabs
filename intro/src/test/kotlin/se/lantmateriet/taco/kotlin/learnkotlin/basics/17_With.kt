@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.junit.jupiter.api.Test

class WithTest {

    @Test
    fun with() {
        class Turtle(val name: String, val age: Int, val speed: String)

        val myTurtle = Turtle("Speedy", 93, "Slower than a snail")
        println("Name:${myTurtle.name}\nAge:${myTurtle.age}\nSpeed: ${myTurtle.speed}")

        // with "väljer" en objektinstans jag vill jobba med i kontextet
        with(myTurtle) {
            println("Name:$name\nAge:$age\nSpeed: $speed")
        }
    }

    @Test
    fun `with - calling multiple methods on an object instance`() {
        class Turtle {
            fun penDown() = println("Down")
            fun penUp() = println("Up")
            fun turn(degrees: Double) = println("turning $degrees")
            fun forward(pixels: Double) = println("forward $pixels")
        }

        val myTurtle = Turtle()
        // with "väljer" en objektinstans jag vill jobba med i kontextet
        with(myTurtle) {
            //draw a 100 pix square
            penDown()
            for (i in 1..4) {
                forward(100.0)
                turn(90.0)
            }
            penUp()
        }
    }


}