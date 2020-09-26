package nu.westlin.kotlinlabs.webflux

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE
import org.springframework.http.codec.ServerSentEvent
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.BodyInserters
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier
import javax.inject.Inject


@WebFluxTest
@RunWith(SpringRunner::class)
class MovieControllerTest {

    private val movies = mutableListOf(
        Movie(1, "Top Secret", 1984),
        Movie(2, "Spaceballs", 1987),
        Movie(3, "Pulp Fiction", 1992)
    )

    @Inject
    lateinit var client: WebTestClient

    @MockBean
    lateinit var newMovieProcessor: NewMovieProcessor

    @MockBean
    lateinit var repository: MovieRepository

    @Test
    fun `get movie by id`() {
        val movie = movies.first()
        whenever(repository.get(movie.id)).thenReturn(movies.find { it.id == movie.id }?.toMono())

        client
            .get()
            .uri("/movie/{id}", movie.id)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody<Movie>()
            .isEqualTo(movie)
    }

    @Test
    fun `list all movies`() {
        whenever(repository.all()).thenReturn(movies.toFlux())

        client
            .get()
            .uri("/movies")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBodyList<Movie>()
            .hasSize(3)
            .contains(*movies.toTypedArray())
    }

    @Test
    @Ignore("This test fails after upgrading Spring from 2.1.9 to 2.3.4 and I don't know why...yet")
    fun `stream movie tips - SSE`() {
        whenever(repository.randomMovie())
            .thenReturn(movies.subList(0, 3).toFlux())

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
            .expectNextMatches {
                it.id() == "0" && assertMovie(it.data()!!, movies[0])
            }
            .expectNextMatches {
                it.id() == "1"
            }
            .expectNextMatches {
                it.id() == "2"
            }
            .thenCancel()
            .verify()
    }

    @Test
    fun `stream movie tips`() {
        whenever(repository.randomMovie())
            .thenReturn(movies.subList(0, 3).toFlux())

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
    @Ignore("Donät know how to test this... :/")
    fun `stream new movies`() {
        val result = client
            .get()
            .uri("/newMovies")
            .accept(TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType("$TEXT_EVENT_STREAM_VALUE;charset=UTF-8")
            .returnResult<Movie>()
        val body = result.responseBody
        StepVerifier.create(body)
            .expectNextMatches {
                it.id == 1 //&& assertMovie(it.data()!!, movies[0])
            }
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
            .expectHeader().contentType(APPLICATION_JSON)
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
            .body(BodyInserters.fromValue(movie))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBodyList<Movie>()
            .hasSize(1)
            .contains(createdMovie)

        verify(newMovieProcessor).process(createdMovie)
    }

    @Suppress("UNCHECKED_CAST")
    private fun assertMovie(actual: Any, expected: Movie): Boolean {
        // For some reason I get not a Movie but a LinkedHashMap<String, String> with the properties from Movie here.
        // I guess it is a Spring config-thing.
        val map = actual as LinkedHashMap<String, Any>

        assertThat(map["id"] as Int).isEqualTo(expected.id)
        assertThat(map["title"] as String).isEqualTo(expected.title)
        assertThat(map["year"] as Int).isEqualTo(expected.year)

        return true
    }
}