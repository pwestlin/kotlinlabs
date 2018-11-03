@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

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
        var nullableString: String? = "b"
        nullableString = null

        assertThatThrownBy { nullableString!!.length }
            .isInstanceOf(KotlinNullPointerException::class.java)
            .hasMessage(null)
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
    fun nullableCheck() {
        class Address(val street: String? = null)
        class Person(val address: Address? = null)
        class Customer(val id: Long, val person: Person?)

        fun getCustomerStreetAddress(customer: Customer?): String? {
            // TODO petves: Gör om till safe-calls
            if (customer != null && customer.person != null && customer.person.address != null && customer.person.address.street != null) {
                return customer.person.address.street
            } else {
                return null
            }

        }

        assertThat(getCustomerStreetAddress(null)).isNull()
        assertThat(getCustomerStreetAddress(Customer(1, Person()))).isNull()
        assertThat(getCustomerStreetAddress(Customer(2, Person(Address())))).isNull()
        assertThat(getCustomerStreetAddress(Customer(3, Person(Address("Lantmäterigatan"))))).isEqualTo("Lantmäterigatan")

        //return customer?.person?.address?.street

        val files = File("/asfgsdhd/cvhndf/jmfghjern").listFiles()    // listFiles() returnerar null om inte pathen kan hittas
        assertThat(files).isNull()

        assertThatThrownBy { files.size }
            .isInstanceOf(NullPointerException::class.java)
            .hasMessage(null)

        assertThat(files?.size).isNull()
        println(files?.size)    // Java: if(files != null) {System.out.println(files.size)}

        println(files?.size ?: "empty")     // Java: *puh*

        var value: String? = null
        value?.let {
            println("Inte null")    // Denna rad kommer aldrig exekveras då value är null
        }

        value = "foo"
        value?.let {
            println("Inte null")
        }

        /* let är alltså samma sak som:
        if(value != null) {
            println("Inte null")
        }
        */
    }

    @Test
    fun `OrElse, OrElseThrow`() {
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