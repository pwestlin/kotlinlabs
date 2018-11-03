package nu.westlin.kotlinlabs.webflux

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import javax.inject.Inject

@WebFluxTest
@RunWith(SpringRunner::class)
class MovieControllerTest {
    val movies = mutableListOf(Movie(1, "Top Secret"), Movie(2, "Spaceballs"))

    @Inject
    lateinit var client: WebTestClient

    @MockBean
    lateinit var repository: MovieRepository

    @Test
    fun `list all movies`() {
        whenever(repository.getAll()).thenReturn(movies)
        println("movies = ${ObjectMapper().writeValueAsString(movies)}")
        client
            .get()
            .uri("/movies")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBodyList(Movie::class.java)
            .hasSize(2)
            .contains(movies[0], movies[1])
            .returnResult()
    }
}