package nu.westlin.kotlinlabs.webflux

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux


fun main() {
    SSEClient().consumeServerSentEvent()
    Thread.sleep(5000)
}

class SSEClient {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun consumeServerSentEvent() {
        val client = WebClient.create("http://localhost:8080/")

        val eventStream = client.get()
            .uri("/movieTip")
            .retrieve()
            .bodyToFlux<Movie>()

        eventStream.subscribe(
            { movie ->
                with(movie) {
                    logger.info("id: $id, title: $title, year: $year")
                }
            },
            { error -> logger.error("Error receiving SSE: {}", error) },
            { logger.info("Completed!!!") })
    }
}