@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExtensionFunctionsTest {

    // Här börjar det bli fränt på riktigt!

    @Test
    fun `extension functions`() {

        val string = "foo"
        assertThat(string.reversed().toUpperCase()).isEqualTo("OOF")

        // Vi vill att det ska finnas en funktion för att reversera strängen och göra den till versaler
        fun String.reverseAndToUpperCase() = this.reversed().toUpperCase()
        assertThat(string.reverseAndToUpperCase()).isEqualTo("OOF")

    }


    // Ni vet den där klassen StringUtils eller DateUtils som man ALLTID har i VARJE projekt?
    // ...den behövs inte längre för man kan använda extension functions istället. :)

    // ...men använd dem klokt!

    // Extension functions är också ett sätt att hålla klasser (läs API) väldigt enkla.
    // Man kan lägga önskade funktioner/operation på en klass i en egen modul för att separera saker.
}