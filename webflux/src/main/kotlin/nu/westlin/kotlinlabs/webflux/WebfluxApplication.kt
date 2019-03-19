@file:Suppress("UnusedMainParameter")

package nu.westlin.kotlinlabs.webflux

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.DirectProcessor
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import kotlin.random.Random

private val logger = LoggerFactory.getLogger(WebfluxApplication::class.java)


@SpringBootApplication
class WebfluxApplication {
/*
    @Bean
    fun routes(movieRepository: MovieRepository) = nest(
        path("/"),
        route(
            GET("/foo"),
            HandlerFunction {
                ServerResponse.ok().syncBody("foo")
            }
        ).andRoute(
            GET("/movie/{id}"),
            HandlerFunction {
                ServerResponse.ok().body(movieRepository.get(it.pathVariable("id").toInt()))
            }
        ).andRoute(
            GET("/movies"),
            HandlerFunction {
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(movieRepository.all())
            }
        ).andRoute(
            GET("/movieTip"),
            HandlerFunction {
                ServerResponse
                    .ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(
                        movieRepository.randomMovie()
                    )
            }
        )
    )
*/
}

fun main(args: Array<String>) {
    runApplication<WebfluxApplication>(*args)


/*
    class MyConsumer {
        fun print(string: String) {
            println("string = $string")
        }
    }

    val consumer = MyConsumer()

    val emitter = EmitterProcessor.create<String>()
    val sink: FluxSink<String> = emitter.sink()
    emitter.publishOn(Schedulers.single())
        .map { it.toUpperCase() }
        .filter { it.startsWith("HELLO") }
        .delayElements(Duration.ofSeconds(1))
        .subscribe { consumer.print(it) }
    sink.next("Hello World!")
    sink.next("Goodbye World!")
    sink.next("Hello Wisconsin!")
    Thread.sleep(4000)
*/

}

@Component
class NewMovieProcessor {
    private val listeners = mutableListOf<NewMovieListener>()

    fun register(listener: NewMovieListener) {
        listeners.add(listener)
    }

    fun process(movie: Movie) {
        listeners.forEach { it.onNewMovie(movie) }
    }

    fun complete() {
        listeners.forEach { it.processComplete() }
    }
}

interface NewMovieListener {
    fun onNewMovie(movie: Movie)
    fun processComplete()
}

@RestController
@RequestMapping("/")
class MovieController(private val movieRepository: MovieRepository, private val newMovieProcessor: NewMovieProcessor) : ApplicationListener<ContextClosedEvent> {
    private val newMovieProcessor2: DirectProcessor<Movie> = DirectProcessor.create()
    private val newMovieFlux: Flux<Movie> = newMovieProcessor2.share()

    @GetMapping("movie/{id}")
    fun get(@PathVariable id: Int) = movieRepository.get(id)

    @GetMapping(path = ["movies"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getAll() = movieRepository.all()

    @GetMapping(path = ["/movieTip"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun movieTip(): Flux<Movie> {
        return movieRepository.randomMovie()
    }

    // Inspired by https://projectreactor.io/docs/core/release/reference/#producing
    @GetMapping(path = ["/newMovies"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun newMovies(): Flux<Movie> {
        return Flux.create { sink ->
            newMovieProcessor.register(object : NewMovieListener {
                override fun processComplete() {
                    sink.complete()
                }

                override fun onNewMovie(movie: Movie) {
                    sink.next(movie)
                }
            })

        }
    }

    // Inspired by https://projectreactor.io/docs/core/release/reference/#reactor.hotCold
    @GetMapping(path = ["/newMovies2"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun newMovies2(): Flux<Movie> {
        return newMovieFlux
    }

    @GetMapping("/movieTip-sse")
    fun streamEvents(): Flux<ServerSentEvent<Movie>> {
        var counter = 0
        return movieRepository.randomMovie()
            .map { sequence ->
                ServerSentEvent.builder<Movie>()
                    .id((counter++).toString())
                    .event("periodic-event")
                    .data(Movie(sequence.id, sequence.title, sequence.year))
                    .build()
            }
    }

    @GetMapping("movies/afterYear/{afterYear}")
    fun getAllAfteryear(@PathVariable afterYear: Int) =
        Flux.fromIterable(movieRepository.getAllAfterYear(afterYear))

    @PostMapping("movie")
    fun create(@RequestBody movie: Movie): Mono<Movie> {
        with(movieRepository.create(movie)) {
            newMovieProcessor.process(this)
            newMovieProcessor2.onNext(movie)
            logger.info("Added movie: $movie")
            return Mono.just(this)
        }
    }

    override fun onApplicationEvent(event: ContextClosedEvent) {
        logger.debug("shutting down")
        this.newMovieProcessor.complete()
        this.newMovieProcessor2.onComplete()
    }
}

@Repository
class MovieRepository {
    private val movies = mutableListOf(
        Movie(1, "Top Secret", 1984),
        Movie(2, "Spaceballs", 1987),
        Movie(3, "Pulp Fiction", 1992),
        Movie(4, "Days of Thunder", 1988)
    )

    fun all(): Flux<Movie> {
        return Flux.fromIterable(movies.toList().sortedBy { it.year }.asReversed())
    }

    fun get(id: Int): Mono<Movie> {
        return Mono.justOrEmpty(movies.single { it.id == id })
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

    fun randomMovie(): Flux<Movie> {
        return Flux.interval(Duration.ofSeconds(1))
            .map { movies[Random.nextInt(movies.size)] }
    }

}

@JsonPropertyOrder("id", "title", "year")
data class Movie constructor(
    val id: Int,
    val title: String,
    val year: Int
)