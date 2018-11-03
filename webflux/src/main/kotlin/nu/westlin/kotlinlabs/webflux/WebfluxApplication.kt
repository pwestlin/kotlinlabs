package nu.westlin.kotlinlabs.webflux

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

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

    @GetMapping("movies")
    fun getAll()=
        Flux.fromIterable(movieRepository.getAll())

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