package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
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
    private fun CoroutineScope.produceraMeddelanden() = produce(capacity = 100) {
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
    fun `fan-out channel`() = runBlocking {
        val producent = produceraMeddelanden()
        repeat(5) { startaKonsument(it + 1, producent) }
        delay(950)
        producent.cancel()
    }

    private fun log(msg: String) {
        println("${Instant.now()}: $msg - ${Thread.currentThread().name}")
    }
}

class FooTest {

    interface Foo {

        fun bar(): String

        companion object : Foo {
            override fun bar(): String {
                return "bar"
            }

        }
    }

    @Test
    fun `asfgfhs dh`() {
        assertThat(Foo.bar()).isEqualTo("bar")
    }
}