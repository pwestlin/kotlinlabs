package nu.westlin.kotlinlabs.webflux

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MovieTest {
    val objectMapper = jacksonObjectMapper()

    @Test
    fun `serialize movie`() {
        val movie = Movie(1, "Foo bar", 1987)
        println("movie = ${movie}")
        val json = objectMapper.writeValueAsString(movie)
        println("json = $json")
        val movieFromJson = objectMapper.readValue(json, Movie::class.java)
        println("movieFromJson = ${movieFromJson}")
        assertThat(movieFromJson).isEqualTo(movie)
    }
}