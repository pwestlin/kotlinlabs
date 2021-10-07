package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines.channels

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nu.westlin.kotlin.learnkotlin.lessbasic.coroutines.log
import org.junit.jupiter.api.Test

// https://kotlinlang.org/docs/channels.html#fan-out
class FainInChannelTest {

    /*
    Multiple coroutines may receive from the same channel, distributing work between themselves.
    Let us start with a producer coroutine that is periodically producing integers (ten numbers per second):
     */

    @Test
    fun `fan-in channel`() = runBlocking {
        val channel = Channel<String>()
        launch { sendString(channel, "Foo?", 200L) }
        launch { sendString(channel, "BAR!", 500L) }
        repeat(10) { // receive first ten   
            log(channel.receive())
        }
        coroutineContext.cancelChildren() // cancel all children to let main finish
    }

    private suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
        while (true) {
            delay(time)
            channel.send(s)
        }
    }
}