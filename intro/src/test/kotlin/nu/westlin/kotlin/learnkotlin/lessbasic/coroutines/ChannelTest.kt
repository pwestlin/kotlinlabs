package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

/**
 * Inspired by:
 * https://play.kotlinlang.org/hands-on/Introduction%20to%20Coroutines%20and%20Channels/08_Channels
 * https://proandroiddev.com/kotlin-coroutines-channels-csp-android-db441400965f
 *
 */
internal class ChannelTest {

    @Test
    fun `do Shit`() {
        val channel = Channel<String>()

        runBlocking {
            launch(Dispatchers.Default + CoroutineName("producer-1")) {
                log("A1 sent")
                channel.send("A1")
                log("A2 sent")
                channel.send("A2")
                log("A done")
            }
            launch(Dispatchers.Default + CoroutineName("producer-2")) {
                log("B1 sent")
                channel.send("B1")
                log("B done")
            }

            launch(Dispatchers.Default + CoroutineName("consumer-1")) {
                for (value in channel) {
                    log("Consumed $value")
                }
            }
            launch(Dispatchers.Default + CoroutineName("consumer-2")) {
                for (value in channel) {
                    log("Consumed $value")
                }
            }
            delay(200)
            channel.close()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test channels`() = runBlockingTest {
        val channel = Channel<String>()
        launch {
            channel.send("First")
            channel.send("Second")
        }
        launch {
            channel.send("Third")
        }
        launch {
            repeat(3) {
                log("Received: ${channel.receive()}")
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test more channels`() = runBlockingTest {
        class Sender(private val channel: SendChannel<String>) {
            suspend fun send(msg: String) {
                channel.send(msg)
            }
        }
        class Receiver(private val channel: ReceiveChannel<String>) {
            suspend fun receive(): String = channel.receive()
        }

        val channel = Channel<String>()
        val sender = Sender(channel)
        val receiver = Receiver(channel)

        launch {
            sender.send("First")
            sender.send("Second")
        }
        launch {
            sender.send("Third")
        }
        launch {
            repeat(3) {
                log("Received: ${receiver.receive()}")
            }
        }
    }
}

/**
 * "Try with reaources" for Channel.
 */
/*
inline fun <E, R> Channel<E>.use(block: (Channel<E>) -> R): R {
    var cause: Throwable? = null
    try {
        return block(this)
    } catch (t: Throwable) {
        cause = t
    } finally {
        close(cause)
    }
}
*/
