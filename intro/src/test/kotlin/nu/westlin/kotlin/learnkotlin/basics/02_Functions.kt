@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package nu.westlin.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

class FunctionsTest {


    // I Kotlin heter det inte metod utan funktion - därav fun
    // Function vs Method vs Procedure: https://blog.kotlin-academy.com/kotlin-programmer-dictionary-function-vs-method-vs-procedure-c0216642ee87
    // "Function returns a value, while procedure doesn’t."
    // "Method is a function associated to an object."
    fun function() {}

    @Test
    fun `ja, en funktion kan faktiskt namnges så här`() {
        // ...men bara i tester är detta acceptabelt!
    }

    @Test
    // Funktion med specad returtyp och "body"
    fun `function with return type and body`() {
        // Japp, man kan ha funktioner inuti en funktion - de kallas local functions
        fun double(int: Int): Int {
            return int * 2
        }

        assertThat(double(3)).isEqualTo(6)
    }

    @Test
    fun `function with expression body and automatic return type`() {
        fun square(int: Int) = int * int

        assertThat(square(3)).isEqualTo(9)
    }

    // Tumregel: Sätt alltid returtypen på publika funktioner

    @Test
    fun `no return type`() {
        fun voidFunctionOrWhat() {
            println("Miss Piggy loves the green frog")
        }

        // Returtypen void saknas i Kotlin, den kallas istället Unit och kan utelämnas vid deklaration.
        assertThat(voidFunctionOrWhat()).isInstanceOf(Unit::class.java)

        // Det finns även en returtyp Nothing men den tar vi i Kodsnacket "Advanced Kotlin" :)
        // (https://blog.kotlin-academy.com/the-beauty-of-kotlin-typing-system-7a2804fe6cf0)
    }

    @Test
    fun varargs() {
        // I Java anger man varargs som "..."
        // Kotlin har varargs som ett keyword
        fun concat(vararg strings: String): String {
            return strings.joinToString(" ")
        }

        assertThat(concat("Varargs", "can", "be", "useful")).isEqualTo("Varargs can be useful")
    }

    @Test
    fun `default values for function params`() {
        fun multiply(a: Int, b: Int = 2) = a * b

        assertThat(multiply(5, 5)).isEqualTo(25)
        assertThat(multiply(5)).isEqualTo(10)

        // Jfr med Javas overloaded methods som anropar varandra
        /*
        int multiply(int a, int b) {
            return a * b
        }
        int multiply(int a) {
            return multiply(a, 2)
        }
        */
    }

    @Test
    fun `named parameters`() {
        fun division(taljare: Int, namnare: Int) = taljare / namnare

        assertThat(division(10, 2)).isEqualTo(5)
        assertThat(division(taljare = 10, namnare = 2)).isEqualTo(5)
        assertThat(division(namnare = 2, taljare = 10)).isEqualTo(5)


        fun reformat(string: String, reverse: Boolean = false, upperCase: Boolean = false): String {
            var formatted = string
            if (reverse) {
                formatted = formatted.reversed()
            }
            if (upperCase) {
                formatted = formatted.uppercase(Locale.getDefault())
            }

            return formatted
        }

        assertThat(reformat("Elsa i Paris")).isEqualTo("Elsa i Paris")
        assertThat(reformat("Elsa i Paris", true)).isEqualTo("siraP i aslE")
        assertThat(reformat("Elsa i Paris", true, true)).isEqualTo("SIRAP I ASLE")
        assertThat(reformat("Elsa i Paris", upperCase = true)).isEqualTo("ELSA I PARIS")
    }

    @Test
    fun `return two values from a function - Pair`() {
        fun twoValuesReturn(fullname: String): Pair<String, String> {
            val names = fullname.split(" ")
            return Pair(names[0], names[1])
        }

        val names = twoValuesReturn("Jürgen DiLeva")
        assertThat(names.first).isEqualTo("Jürgen")
        assertThat(names.second).isEqualTo("DiLeva")

        val (first, last) = twoValuesReturn("Elvis Costello")
        assertThat(first).isEqualTo("Elvis")
        assertThat(last).isEqualTo("Costello")
    }

    @Test
    fun `return three values from a function - Triple`() {
        fun threeValuesReturn(fullname: String): Triple<String, String, String> {
            val names = fullname.split(" ")
            return Triple(names[0], names[1], names[2])
        }

        val (title, firstname, lastname) = threeValuesReturn("Mr. Boom Bastic")
        assertThat(title).isEqualTo("Mr.")
        assertThat(firstname).isEqualTo("Boom")
        assertThat(lastname).isEqualTo("Bastic")
    }


    // Överkurs

    @Test
    fun `list extension`() {
        fun <T> List<T>.doStuff(operations: (List<T>) -> Unit) {
            operations(this)
        }

        val listOfStrings = listOf("Foo", "bar", "färdtjänst")
        listOfStrings.doStuff {
            println("size = ${it.size}")
            println("first() = ${it.first()}")
            println("last() = ${it.last()}")
        }
    }

    @Test
    fun `list extension with a receiver`() {
        fun <T> List<T>.doStuff(operations: List<T>.() -> Unit) {
            operations()
        }

        val listOfStrings = listOf("Foo", "bar", "färdtjänst")
        listOfStrings.doStuff {
            println("size = $size")
            println("first() = ${first()}")
            println("last() = ${last()}")
        }
    }

}


// "Message: $message" -> String template
fun topLevelFunctionUtil(message: String) {
    println("Message: $message")
}

object ObjectUtil {
    fun log(message: String) {
        println("Message: $message")
    }
}

class UtilTest {
    @Test
    fun `top level functions`() {
        // Japp, man kan också ha lokala klasser i funktioner
        class ClassUtil {
            fun log(message: String) {
                println("Message: $message")
            }
        }
        // Klasser kan inte ha statiska metoder i Kotlin så vi måste först skapa en instans av klassen - blä!
        ClassUtil().log("Foo")

        // Lite snyggare
        ObjectUtil.log("Foo")

        // men det vi egentligen vill göra är att bara anropa en hjälpfunktion, eller hur?
        topLevelFunctionUtil("Foo")
    }


}


