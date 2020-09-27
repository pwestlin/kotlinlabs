@file:Suppress("UNUSED_VARIABLE")

package nu.westlin.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LazyTest {

    class Foo {

        val bar: String by lazy {
            println("Initialized!")
            "baraboom"
        }
    }

    @Test
    fun `test lazy`() {
        val foo = Foo()

        assertThat(foo.bar).isEqualTo("baraboom")
    }
}

