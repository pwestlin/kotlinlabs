@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.junit.jupiter.api.Test

class WithTest {

    @Test
    fun with() {
        class Turtle(val name: String, val age: Int, val characteristics: String)

        val myTurtle = Turtle("Speedy", 93, "Slower than a snail")
        println("Name:${myTurtle.name}\nAge:${myTurtle.age}\nSpeed: ${myTurtle.characteristics}")

        // with "väljer" en objektinstans jag vill jobba med i kontextet
        with(myTurtle) {
            println("Name:$name\nAge:$age\nSpeed: $characteristics")
        }
    }

    @Test
    fun `with - calling multiple methods on an object instance`() {
        class Pen {
            fun down() = println("Down")
            fun up() = println("Up")
            fun turn(degrees: Double) = println("turning $degrees")
            fun forward(pixels: Double) = println("forward $pixels")
        }

        val pen = Pen()
        with(pen) {
            //draw a 100 pix square
            down()
            for (i in 1..4) {
                forward(100.0)
                turn(90.0)
            }
            up()
        }
    }


}