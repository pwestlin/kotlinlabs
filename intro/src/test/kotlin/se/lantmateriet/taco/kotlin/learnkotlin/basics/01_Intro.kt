@file:Suppress("PackageName", "unused", "UnusedMainParameter")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

// main
fun main(args: Array<String>) {
    println("Hello, Wisconsin!")
}

// Från och med Kotlin 1.3 kan main deklareras utan parametrar
fun main() {
    println("Pirkko Määttä!")
}

fun thisIsATopLevelFunction(i: Int): Int {
    return i * 2
}

class Foo

// Ja, man kan ha flera klasser per Kotlin-fil
class Bar

data class Bil(val modell: String?)

class BilTest {

    // Extension function (se 15_ExtensionFunctions.kt)
    private fun Bil.finModell() = this.modell?.let { "Modell: $modell" } ?: "ingen"

    @Test
    fun `fin modell`() {
        assertThat(Bil("Skoda").finModell()).isEqualTo("Modell: Skoda")
        assertThat(Bil(null).finModell()).isEqualTo("ingen")
    }
}
