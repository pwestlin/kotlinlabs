@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package nu.westlin.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class ExtensionFunctionsTest {

    // Här börjar det bli fränt på riktigt!

    private val String.lastChar: Char
        get() = this.toCharArray().last()

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
    // Man kan lägga önskade funktioner/operationer på en klass i en egen modul för att separera saker.

    @Test
    fun `extension properties`() {
        assertThat("Foobar".lastChar).isEqualTo('r')
    }


    // Men om String är null då?
    private fun String?.containsSubstring(substring: String): Boolean = this?.contains(substring) ?: false

    @Test
    fun `extension function och null`() {
        val string: String? = null
        assertThat(string.containsSubstring("foo")).isFalse
    }


    // Inspiration: https://proandroiddev.com/keep-your-interfaces-simple-e025d515e3b9
    interface AnInterface {
        fun isTrue(): Boolean
    }

    @Test
    fun `try AnInterface isTrue`() {
        class AClass: AnInterface {
            override fun isTrue() = true
        }

        assertThat(AClass().isTrue()).isTrue
    }

    @Test
    fun `try AnInterface isFalse`() {
        fun AnInterface.isFalse() = isTrue().not()

        class AClass: AnInterface {
            override fun isTrue() = true
        }

        assertThat(AClass().isFalse()).isFalse
    }
}
