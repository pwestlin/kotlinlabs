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

        val result = returnString().let {
            println("length of '$it' is ${it.length}")
            println("Upper case of '$it' is ${it.toUpperCase()}")
        }
        assertThat(result).isInstanceOf(Unit::class.java)
    }
}

class ApplyTest {

    // apply is useful if you need to call several methods on an object without caring for their return, like setters.

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

    // also is useful if you have a value that you need to return, but before returning you also need to use it to do something else.
    @Test
    fun `also it`() {
        val result = Person("Rory", "Racer")
            .also { println(it.firstname) }
            .also { println(it.surname) }
        assertThat(result).isInstanceOf(Person::class.java)
    }
}

class WithTest {

    @Test
    fun `with object`() {
        val string = with(Person("Rory", "Racer")) {
            assertThat(firstname).isEqualTo("Rory")
            assertThat(surname).isEqualTo("Racer")
            "foo"
        }
        assertThat(string).isEqualTo("foo")
    }
}

class RunTest {

    // run is useful if at the end calling a few methods on the object and then one that return something else.

    @Test
    fun `scope local variable`() {
        val length = "Apple".run {
            println(this)
            this.length
        }
        assertThat(length).isEqualTo(5)
    }
}