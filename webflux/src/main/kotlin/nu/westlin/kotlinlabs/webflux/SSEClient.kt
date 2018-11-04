package nu.westlin.kotlinlabs.webflux

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import java.time.LocalTime


fun main() {
    SSEClient().consumeServerSentEvent()
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
                logger.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                    LocalTime.now(), content.event(), content.id(), content.data())
            },
            { error -> logger.error("Error receiving SSE: {}", error) },
            { logger.info("Completed!!!") })
    }
}