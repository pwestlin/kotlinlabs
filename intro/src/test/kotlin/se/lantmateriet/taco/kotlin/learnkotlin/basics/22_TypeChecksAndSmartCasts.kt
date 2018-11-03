@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.chrono.ChronoLocalDate

class TypeChecksAndSmartCastsTest {

    @Test
    fun `type checks`() {
        fun printLength(obj: Any) {
            if (obj is String) {
                println(obj.length)
            }

            if (obj !is String) { // same as !(obj is String)
                println("Not a String")
            } else {
                println(obj.length)
            }
        }

        printLength("foo")
        printLength(123)
    }

    @Test
    fun `smart casts`() {
        //val date: ChronoLocalDate? = LocalDate.now()
        val date: ChronoLocalDate? = null
        if (date != null) {
            println(date.isLeapYear)
        }

        if (date != null && date.isLeapYear) {
            println("It's a leap year!")
        }

        if (date == null || !date.isLeapYear) {
            println("There's no Feb 29 this year...")
        }

        if (date is LocalDate) {
            val month = date.monthValue
            println(month)
        }

        /////////////////////////

        fun foo(obj: Any) {
            if (obj is String) {
                println("obj.length = ${obj.length}")
            }
        }

        foo("sgsx")
        foo(72.3)
    }


}