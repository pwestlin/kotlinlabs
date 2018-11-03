package nu.westlin.kotlinlabs.webflux

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
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
        Mono.just(movieRepository.getAll())

    @PostMapping("movie")
    fun create(@RequestBody movie: Movie): Mono<Movie> {
        return Mono.just(movieRepository.create(movie.title))
    }
}

@Repository
class MovieRepository() {
    private val movies = mutableListOf(Movie(1, "Top Secret"), Movie(2, "Spaceballs"))

    fun getAll(): List<Movie> {
        return movies.toList()
    }

    fun get(id: Int): Movie? {
        return movies.single { it.id == id }
    }

    fun get(title: String): Movie? {
        return movies.single { it.title == title }
    }

    fun create(title: String): Movie {
        if (!movies.none { it.title == title }) {
            throw RuntimeException("Movie with title $title already exist")
        }

        val movie = Movie(createId(), title)
        movies.add(movie)

        return movie
    }

    private fun createId(): Int {
        val maxBy = movies.maxBy { it.id }
        return if (maxBy != null) maxBy.id + 1 else 1
    }

}

data class Movie @JsonCreator constructor(@JsonProperty("id") val id: Int, @JsonProperty("title") val title: String)