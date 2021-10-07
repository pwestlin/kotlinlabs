package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.Instant

/**
 * Testar "fan-out channels: https://kotlinlang.org/docs/channels.html#fan-out
 */
class FanOutChannelTest {

    /*
     Multiple coroutines may receive from the same channel, distributing work between themselves.
     */

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun CoroutineScope.produceraMeddelanden() = produce<Char>(capacity = 100) {
        var code = 65
            while (true) {
                Char(code++).let { char ->
                    log("Producerade $char")
                    send(char)
                    delay(10)
                }
            }
    }

    private fun CoroutineScope.startaKonsument(id: Int, channel: ReceiveChannel<Char>) = launch(Dispatchers.Default) {
        for (meddelande in channel) {
            log("Konsument #$id tog emot meddlande \"$meddelande\"")
            delay(100)
        }
    }

    @Test
    fun `fan-out channel`() = runBlocking<Unit> {
        val producent = produceraMeddelanden()
        repeat(5) { startaKonsument(it + 1, producent) }
        delay(950)
        producent.cancel()
    }

    private fun log(msg: String) {
        println("${Instant.now()}: $msg - ${Thread.currentThread().name}")
    }
}