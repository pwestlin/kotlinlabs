@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName", "SENSELESS_COMPARISON")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.File

class NullsTest {

    @Test
    fun nulls() {
        var string: String = "a"
        //string = null     // Kompileringsfel för var string får inte vara null

        // Notera ? efter String
        val nullableString: String? = null

        // !! -> Ge mig värdet i variabeln för jag VET att det inte är null (unsafe call)
        assertThatThrownBy { nullableString!!.length }
            .isInstanceOf(KotlinNullPointerException::class.java)
            .hasMessage(null)

        // !! finns egentligen bara till pga att Kotlin ska
        // vara Java-kompatibelt
        // I rena Kotlin-projekt existerar unsafe calls oerhört sällan.
        // I Java är alla anrop unsafe.
    }

    @Test
    fun `nullable Boolean`() {
        val b: Boolean? = null
        if (b == true) {
            println("true")
        } else {
            println("false or null")
        }
    }

    @Test
    fun `en liten försmak av smart casts`() {
        val nullableString: String? = "Bosse Ringholm bor inte här längre"
        if (nullableString != null) {
            println(nullableString.length)
        }
    }

    @Test
    fun nullableCheck() {
        class Address(val street: String? = null)
        class Person(val address: Address? = null)
        class Customer(val id: Long, val person: Person?)

        fun getCustomerStreetAddress(customer: Customer?): String? {
            if (customer != null && customer.person != null && customer.person.address != null && customer.person.address.street != null) {
                return customer.person.address.street
            } else {
                return null
            }
        }

        fun betterGetCustomerStreetAddress(customer: Customer?): String? {
            // Safe calls
            return customer?.person?.address?.street
        }

        assertThat(getCustomerStreetAddress(null)).isNull()
        assertThat(getCustomerStreetAddress(Customer(1, Person()))).isNull()
        assertThat(getCustomerStreetAddress(Customer(2, Person(Address())))).isNull()
        assertThat(getCustomerStreetAddress(Customer(3, Person(Address("Lantmäterigatan"))))).isEqualTo("Lantmäterigatan")

        val customer: Customer? = null
        assertThat(customer?.person?.address?.street).isNull()


        val files = File("/asfgsdhd/cvhndf/jmfghjern").listFiles()    // listFiles() returnerar null om inte pathen kan hittas
        assertThat(files).isNull()

        assertThatThrownBy { files.size }
            .isInstanceOf(NullPointerException::class.java)
            .hasMessage(null)

        assertThat(files?.size).isNull()
        println(files?.size)    // Java: if(files != null) {System.out.println(files.size)}

        assertThat(files?.size ?: "empty").isEqualTo("empty")   // Java: *puh*

        // Vi hade kunnat göra det snyggare genom att från början:
        // val files:Array<File>? = File("/asfgsdhd/cvhndf/jmfghjern").listFiles()    // listFiles() returnerar null om inte pathen kan hittas


        var value: String? = null
        value?.let {
            println("Inte null")    // Denna rad kommer aldrig exekveras då value är null
        }

        value = "foo"
        value?.let { foo ->
            println("Inte null utan $foo")
        }

        // ?.let är alltså samma sak som
        if (value != null) {
            println("Inte null")
        }
    }

    @Test
    fun `OrElse, OrElseThrow i Java`() {
        fun canReturnNull(string: String?) = string

        // ?: i kotlin är samma som Javas .orElse(...)
        assertThat(canReturnNull("foo") ?: "null").isEqualTo("foo")
        assertThat(canReturnNull(null) ?: "null").isEqualTo("null")

        // ...och faktiskt även samma som Javas orElseThrow
        assertThatThrownBy { canReturnNull(null) ?: throw RuntimeException("Jag ville inte ha null!") }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Jag ville inte ha null!")

    }

}