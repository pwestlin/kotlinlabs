package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

data class Person(var firstname: String? = null, var surname: String? = null)

class LetTest {

    @Test
    fun `let if not null`() {
        fun prettyDouble(int: Int?): String {
            // Only double int if it is not null
            return int?.let { (it * 2).toString() } ?: "null can't be doubled"
        }

        assertThat(prettyDouble(31)).isEqualTo("62")
        assertThat(prettyDouble(null)).isEqualTo("null can't be doubled")
    }

    @Test
    fun `let instead of local val`() {
        fun returnString() = "foo"

        returnString().let {
            println("length of '$it' is ${it.length}")
            println("Upper case of '$it' is ${it.toUpperCase()}")
        }
    }
}

class ApplyTest {
    @Test
    fun `apply it`() {
        val person = Person().apply {
            firstname = "Rory"
            surname = "Racer"
        }

        assertThat(person.firstname).isEqualTo("Rory")
        assertThat(person.surname).isEqualTo("Racer")
    }
}

class AlsoTest {
    @Test
    fun `also it`() {
        Person("Rory", "Racer")
            .also { println(it.firstname) }
            .also { println(it.surname) }
    }
}

class WithTest {

    @Test
    fun `with object`() {
        with(Person("Rory", "Racer")) {
            assertThat(firstname).isEqualTo("Rory")
            assertThat(surname).isEqualTo("Racer")
        }
    }
}

class RunTest {

    @Test
    fun `scope local variable`() {
        // TODO petves: impl
    }
}