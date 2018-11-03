@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException

class CheckedExceptionsTest {

    @Test
    fun checkedExceptions() {
        // Kotlin does not have checked exceptions - YAAAAAAAAAAAAAAAAY!
        // ...men hur sjutton blir det när man anropar en Java-metod som kastar ett checked exception?

        // readBytes() kastar ett IOException (checked exception) om en fil inte kan tas bort
        //println(File("/d2362/sdhdhs/xbryud/sdfghsdg.txt").readBytes())

        // ...men om jag vill ta hand om felet då?
        try {
            println(File("/d2362/sdhdhs/xbryud/sdfghsdg.txt").readBytes())
        } catch (e: IOException) {
            // Felhantering
        }
    }


}