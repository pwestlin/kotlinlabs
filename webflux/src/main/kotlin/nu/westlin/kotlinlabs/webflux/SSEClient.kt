package nu.westlin.kotlinlabs.webflux

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import java.time.LocalTime


/**
 * Consumes Server-Sent Events (SSE) from endpoint in MovieController.
 */
fun main() {
    SSEClient().consumeServerSentEvent()

    // Sleep for a while to get some events (which arrives in another thread)
    Thread.sleep(5000)
}

class SSEClient {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun consumeServerSentEvent() {
        val client = WebClient.create("http://localhost:8080/")

        val eventStream = client.get()
            .uri("/movieTip-sse")
            .retrieve()
            .bodyToFlux<ServerSentEvent<Movie>>()

        eventStream.subscribe(
            { content ->
                run {
                    val movie = content.data()!!

                    logger.info("Time: ${LocalTime.now()} - event: name[${content.event()}], id [${content.id()}], content[$movie] ")
                }
            },
            { error -> logger.error("Error receiving SSE: {}", error) },
            { logger.info("Completed!!!") })
    }
}