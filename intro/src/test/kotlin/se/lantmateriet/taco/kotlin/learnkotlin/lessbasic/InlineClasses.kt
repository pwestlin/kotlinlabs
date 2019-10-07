package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OrdinaryClassesTest {

    @Test
    fun `ordinary class`() {
       class Person(val firstname: String, val lastname: String)

        with(Person("Jane", "Doh")) {
            assertThat(this.firstname).isEqualTo("Jane")
            assertThat(this.lastname).isEqualTo("Doh")
        }

        // But what if we accidentally switch firstname and lastname?
        // Person("Doh", "Jane")
        // Both arguments is of type String so the compiler happily says "Yes!".
        // Se below for solution!
    }
}

inline class Firstname(val value: String)
inline class Lastname(val value: String)

internal class InlineClassesTest {

    @Test
    fun `inline class`() {
       class Person(val firstname: Firstname, val lastname: Lastname)

        with(Person(Firstname("Jane"), Lastname("Doh"))) {
            assertThat(this.firstname.value).isEqualTo("Jane")
            assertThat(this.lastname.value).isEqualTo("Doh")
        }
    }
}
