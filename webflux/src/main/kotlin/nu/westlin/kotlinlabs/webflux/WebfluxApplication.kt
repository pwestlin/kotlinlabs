package nu.westlin.kotlinlabs.webflux

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration.ofMillis

@SpringBootApplication
class WebfluxApplication

fun main(args: Array<String>) {
    runApplication<WebfluxApplication>(*args)
}


@RestController
@RequestMapping("/")
class MovieController(private val movieRepository: MovieRepository) {

    @GetMapping("movie/{id}")
    fun get(@PathVariable id: Int) = Mono.justOrEmpty(movieRepository.get(id))

    @GetMapping(path = ["movies"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getAll() = Flux.fromIterable(movieRepository.getAll())

    // TODO: Can't make this work :|
    @GetMapping(path = ["movies"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getAllStream(): Mono<ServerResponse> {
        val usersFlux = Flux.fromIterable(movieRepository.getAll())
        val userStream = Flux
            .zip(Flux.interval(ofMillis(100)), usersFlux.repeat())
            .map { it.t2 }
        return ok().bodyToServerSentEvents(userStream)
    }

    @GetMapping("movies/afterYear/{afterYear}")
    fun getAllAfteryear(@PathVariable afterYear: Int) =
        Flux.fromIterable(movieRepository.getAllAfterYear(afterYear))

    @PostMapping("movie")
    fun create(@RequestBody movie: Movie): Mono<Movie> {
        return Mono.just(movieRepository.create(movie))
    }
}

@Repository
class MovieRepository() {
    private val movies = mutableListOf(Movie(1, "Top Secret", 1984), Movie(2, "Spaceballs", 1987))

    fun getAll(): List<Movie> {
        return movies.toList()
    }

    fun get(id: Int): Movie? {
        return movies.single { it.id == id }
    }

    fun get(predicate: (Movie) -> Boolean): List<Movie> {
        return movies.filter(predicate)
    }

    fun get(title: String): Movie? {
        return movies.single { it.title == title }
    }

    fun create(movie: Movie): Movie {
        if (!movies.none { it.title == movie.title }) {
            throw RuntimeException("Movie with title ${movie.title} already exist")
        }

        val newMovie = Movie(createId(), movie.title, movie.year)
        movies.add(newMovie)

        return newMovie
    }

    private fun createId(): Int {
        val maxBy = movies.maxBy { it.id }
        return if (maxBy != null) maxBy.id + 1 else 1
    }

    fun getAllAfterYear(year: Int): List<Movie> = movies.filter { it.year > year }

}

data class Movie @JsonCreator constructor(
    @JsonProperty("id") val id: Int,
    @JsonProperty("title") val title: String,
    @JsonProperty("year") val year: Int
)