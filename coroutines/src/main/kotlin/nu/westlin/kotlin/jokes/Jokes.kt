package nu.westlin.kotlin.jokes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@Suppress("unused")
@RestController
@RequestMapping("/")
class JokesController(private val service: JokesService) {

    @GetMapping(value = ["joke"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun joke() = service.randomJoke()

    @GetMapping(value = ["jokes"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun jokes() = service.randomJokes(10)
}


@Service
class JokesService(private val repository: JokesRepository) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun randomJoke() = repository.randomJoke()

    fun randomJokes(noJokes: Int): List<Joke> {
        var jokes: List<Joke> = listOf()
        runBlocking {
            val execTime = measureTimeMillis {
                jokes = (1..noJokes).map {
                    async(Dispatchers.IO) { repository.randomJoke() }
                }.awaitAll()
            }
            logger.info("It took $execTime ms to retrieve $noJokes jokes")
        }

        return jokes
    }
}

@Configuration
class WebConfiguration(private val restTemplateBuilder: RestTemplateBuilder) {

    @Bean
    fun restTemplate() = restTemplateBuilder.build()!!
}

@Repository
class JokesRepository(@Inject private val restTemplate: RestTemplate) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun randomJoke(): Joke {
        val joke = (restTemplate.getForObject("http://official-joke-api.appspot.com/random_joke", Joke::class.java)
            ?: throw RuntimeException("Could not find joke"))
        logger.info("joke = $joke")
        return joke
    }

}

data class Joke(val id: String, val type: String, val setup: String, val punchline: String)