package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
