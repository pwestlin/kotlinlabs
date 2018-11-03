@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.junit.Test
import kotlin.properties.Delegates


abstract class Utbytesobjekt(
    val objektidentitet: String,
    val objektversion: Int = 1
)

class Fastighet(
    objektidentitet: String,
    objektversion: Int = 1,
    val status: String) : Utbytesobjekt(objektidentitet, objektversion)

class Samfallighet() {
    constructor(init: Samfallighet.() -> Unit) : this() {
        this.init()
    }

    var objektidentitet: String by Delegates.notNull()
    var objektversion: Int by Delegates.notNull()
    var status: String by Delegates.notNull()
}

class Gemensamhetsanlaggning private constructor(
    objektidentitet: String,
    objektversion: Int = 1,
    val status: String) : Utbytesobjekt(objektidentitet, objektversion) {
    companion object Builder {
        var objektidentitet: String by Delegates.notNull()
        var objektversion: Int = 1
        var status: String by Delegates.notNull()
        fun build(): Gemensamhetsanlaggning {
            return Gemensamhetsanlaggning(objektidentitet, objektversion, status)
        }
    }

}

class BuildersTest {

    @Test
    fun `fastighet`() {
        val fastighet = Fastighet("uuid1", 1, "levande")
        val fastighet2 = Fastighet(objektidentitet = "uuid2", objektversion = 1, status = "levande")
        val fastighet3 = Fastighet(objektidentitet = "uuid3", status = "levande")
    }

    @Test
    fun `samfällighet`() {
        val samfallighet2 = Samfallighet()
        val samfallighet = Samfallighet {
            objektidentitet = "uuid1"
            objektversion = 1
            status = "avregistrerad"
        }
        // Mutable var ju inte direkt vad vi var ute efter... :(
        samfallighet.status = ""
    }

    @Test
    fun `gemensamhetsanläggning`() {
        val ga = Gemensamhetsanlaggning.apply {
            objektidentitet = "uuid1"
            //objektversion = 2
            status = "levande"
        }.build()   // vi måste göra build() annars får vi tillbaka en instans av builder och det vill vi (oftast) inte

        println("ga.objektidentitet = ${ga.objektidentitet}")
        println("ga.objektversion = ${ga.objektversion}")
        println("ga.status = ${ga.status}")

        //ga.status = ""
    }
}