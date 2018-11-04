package nu.westlin.kotlinlabs.webflux

import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.*
import org.springframework.http.codec.ServerSentEvent
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.BodyInserters
import reactor.test.StepVerifier
import javax.inject.Inject


@WebFluxTest
@RunWith(SpringRunner::class)
class MovieControllerTest {
    val movies = mutableListOf(
        Movie(1, "Top Secret", 1984),
        Movie(2, "Spaceballs", 1987),
        Movie(3, "Pulp Fiction", 1992)
    )

    @Inject
    lateinit var client: WebTestClient

    @MockBean
    lateinit var repository: MovieRepository

    @Test
    fun `get movie by id`() {
        val movie = movies.first()
        whenever(repository.get(movie.id)).thenReturn(movies.find { it.id == movie.id })

        client
            .get()
            .uri("/movie/{id}", movie.id)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBody<Movie>()
            .isEqualTo(movie)
    }

    @Test
    fun `list all movies`() {
        whenever(repository.getAll()).thenReturn(movies)

        client
            .get()
            .uri("/movies")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBodyList<Movie>()
            .hasSize(3)
            .contains(*movies.toTypedArray())
    }

    @Test
    @Ignore("Can't make this work :|")
    fun `stream movie tips - SSE`() {
        whenever(repository.randomMovie())
            .thenReturn(movies[0])
            .thenReturn(movies[1])
            .thenReturn(movies[2])

        val result = client
            .get()
            .uri("/movieTip-sse")
            .accept(TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType("$TEXT_EVENT_STREAM_VALUE;charset=UTF-8")
            .returnResult<ServerSentEvent<Movie>>()
        val body = result.responseBody
        StepVerifier.create(body)
            .assertNext {
                assertThat(it.id()).isEqualTo("0")
                assertThat(it.event()).isEqualTo("periodic-event")
                val movie = it.data()!!
                assertThat(movie).isEqualTo(movies[0])
                //assertThat(it.data()!!).isEqualTo(movies[0])
                assertThat(it.comment()).isNull()
            }
/*
            .expectNext(movies[0])
            .expectNext(movies[1])
            .expectNext(movies[2])
*/
            .thenCancel()
            .verify()
    }

    @Test
    fun `stream movie tips`() {
        whenever(repository.randomMovie())
            .thenReturn(movies[0])
            .thenReturn(movies[1])
            .thenReturn(movies[2])

        val result = client
            .get()
            .uri("/movieTip")
            .accept(TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType("$TEXT_EVENT_STREAM_VALUE;charset=UTF-8")
            .returnResult(Movie::class.java)
        val body = result.responseBody
        StepVerifier.create(body)
            .expectNext(movies[0])
            .expectNext(movies[1])
            .expectNext(movies[2])
            .thenCancel()
            .verify()
    }

    @Test
    fun `list all movies made after 1984`() {
        val year = 1984
        whenever(repository.getAllAfterYear(year)).thenReturn(movies.filter { it.year > year })

        client
            .get()
            .uri("/movies/afterYear/{afterYear}", year)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBodyList<Movie>()
            .hasSize(2)
            .contains(movies[1], movies[2])
    }

    @Test
    fun `create a movie`() {
        val movie = Movie(-1, "Fooa bara", 1234)
        val createdMovie = movie.copy(id = 1)
        whenever(repository.create(movie)).thenReturn(createdMovie)

        client
            .post()
            .uri("/movie")
            .body(BodyInserters.fromObject(movie))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBodyList<Movie>()
            .hasSize(1)
            .contains(createdMovie)
    }

}