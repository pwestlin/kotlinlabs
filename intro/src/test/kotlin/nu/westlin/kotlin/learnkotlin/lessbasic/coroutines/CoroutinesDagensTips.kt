package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CoroutinesDagensTips {

    @Test
    fun `kör ett par routines`() {
        fun routine(id: Int, delay: Long) {
            log("Routine $id start")
            Thread.sleep(delay)
            log("Routine $id slut")
        }

        log("Start")
        routine(1, 1000)
        routine(2, 2000)
        log("Slut")
    }

    @Test
    fun `kör ett par coroutines`() {
        suspend fun routine(id: Int, delay: Long) {
            log("Routine $id start")
            delay(delay)
            log("Routine $id slut")
        }

        runBlocking {
            log("Start")
            routine(1, 1000)
            routine(2, 2000)
            log("Slut")
        }
    }

    @Test
    fun `kör ett par coroutines asynkront`() {
        suspend fun routine(id: Int, delay: Long) {
            log("Routine $id start")
            delay(delay)
            log("Routine $id slut")
        }

        runBlocking {
            log("Start")
            launch { routine(1, 1000) }
            launch { routine(2, 2000) }
            log("Slut")
        }
    }

    @Test
    fun `kör ett par coroutines asynkront i flera trådar`() {
        suspend fun routine(id: Int, delay: Long) {
            log("Routine $id start")
            delay(delay)
            log("Routine $id slut")
        }

        runBlocking(Dispatchers.Default) {
            log("Start")
            launch { routine(1, 1000) }
            launch { routine(2, 2000) }
            log("Slut")
        }
    }

    @Test
    fun `kör ett gäng coroutines asynkront och invänta svar mha async-await`() {
        data class Användare(val id: Int, val namn: String)

        suspend fun hämtaAnvändareFrånEnSlöTjänstPåInternet(id: Int): Användare {
            log("Hämtar användare med id $id")
            delay(1000)
            return Användare(id, "Användare $id")
        }

        val användaridn = 1..100
        log("användaridn = $användaridn")

        runBlocking {
            log(
                användaridn
                    .map { async { hämtaAnvändareFrånEnSlöTjänstPåInternet(it) } }
                    .awaitAll()
                    .toString()
            )
        }
    }

    @Test
    fun `Testa cancel`() {
        suspend fun doStuff(delay: Long, throwException: Boolean) {
            log("Start doing stuff and the delays for $delay ms")
            delay(delay)
            if (throwException) {
                log("Throwing an exception")
                throw RuntimeException("Foo")
            }
            log("Done doing stuff after delaying for $delay ms")
        }

        log("Start")
        runBlocking {
            launch { doStuff(1000, false) }
            launch { doStuff(2000, false) }
            launch { doStuff(1000, false) }
            GlobalScope.launch { doStuff(3000, false) }
        }
        log("Done")
    }
}

class Foo {
    val logger = LoggerFactory.getLogger(Foo::class.java)
    val logger2 = LoggerFactory.getLogger(javaClass)
    val logger3 = getLogger<Foo>()

    @Test
    fun `sdtgas fgsdf ag`() {
        logger.info("logger")
    }
}

inline fun <reified T> getLogger(): Logger = LoggerFactory.getLogger(T::class.java)