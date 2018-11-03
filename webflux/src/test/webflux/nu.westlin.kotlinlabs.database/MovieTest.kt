package nu.westlin.kotlinlabs.webflux

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

class MovieTest {
    val objectMapper = ObjectMapper()

    @Test
    fun `serialize movie`() {
        val string = objectMapper.writeValueAsString(Movie(1, "Foo bar"))
        println("string = ${string}")
    }
}