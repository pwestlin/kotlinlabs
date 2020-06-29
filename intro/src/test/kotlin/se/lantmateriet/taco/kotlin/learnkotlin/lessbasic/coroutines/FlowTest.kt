package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.DEFAULT_CONCURRENCY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureTimeMillis

@Suppress("BlockingMethodInNonBlockingContext")
@FlowPreview
@ExperimentalCoroutinesApi
internal class FlowTest {

    private fun theSimpsonsFlow() = flow {
        log("Starting flow...")
        emit("Homer")
        delay(20)
        emit("Marge")
        delay(100)
        emit("Bart")
        delay(10)
        emit("Lisa")
        delay(30)
        emit("Maggie")
        log("Flow finished!")
    }

    @Test
    fun `flow of the Simpsons`() = runBlocking {
        theSimpsonsFlow().collect {
            log(it)
        }
    }

    // Custom operator on Flow
    private fun Flow<String>.uppercase(): Flow<String> {
        return flow {
            collect {
                emit(it.toUpperCase())
            }
        }
    }

    @Test
    fun `uppercase family`() = runBlocking {
        theSimpsonsFlow().uppercase().collect {
            log(it)
        }
    }

    @Test
    fun `flow of the Simpsons on separate threads`() = runBlocking {

        theSimpsonsFlow()
            .flowOn(Dispatchers.IO)
            .map { it.toUpperCase().also { _ -> log(it) } }
            .flowOn(Dispatchers.Default)
            .filter { it.startsWith("M").also { _ -> log(it) } }
            .flowOn(Dispatchers.Unconfined)
            .collect { log(it) }
    }

    @Test
    fun `flow of the Simpsons on separate threads and coroutines`() = runBlocking {
        fun toUppercase(value: String): String = value.toUpperCase().also { log(it) }
        fun startsWith(value: String, start: String): Boolean = value.startsWith(start).also { log(value) }

        theSimpsonsFlow()
            .flowOn(Dispatchers.IO)
            .map { toUppercase(it) }
            .flowOn(Dispatchers.Default)
            .filter { startsWith(it, "M") }
            .flowOn(Dispatchers.Unconfined)
            .collect { log(it) }
    }

    @Disabled("slow")
    @Test
    fun `as flow`() = runBlocking {
        val emitter = listOf(1, 2, 3, 4, 5).asFlow()
            .onStart { println("Flow started") }
            //.onEach { value -> println("value = $value") }
            .onCompletion { println("Flow completed") }

        emitter.buffer(4).collect {
            println("it = $it")
        }
    }

    @Disabled("slow")
    @Test
    fun `flow without buffer is sequential`() = runBlocking {
        val execTime = measureTimeMillis {
            flowOf("A", "B", "C")
                .onEach {
                    delay(1000)
                    log("1$it")
                }
                .collect { log("2$it") }
        }

        println("execTime = $execTime")
    }

    @Disabled("slow")
    @Test
    fun `flow with buffer is async`() = runBlocking {
        val execTime = measureTimeMillis {
            flowOf("A", "B", "C")
                .onEach {
                    log("1$it")
                    delay(1000)
                }
                .buffer()  // <--------------- buffer between onEach and collect
                .collect { log("2$it") }
        }

        println("execTime = $execTime")
    }

    @Disabled("slow")
    @Test
    fun `fast emitter and slow collector`() = runBlocking {
        val emitter = (1..3)
            .asFlow()
            .onEach {
                delay(1000)
                log("Emitted $it")
            }

        println("Exec time = ${measureTimeMillis {
            emitter
                .collect {
                    log("Start collecting $it")
                    delay(3000)
                    log("End collecting $it")
                }
        }
        }")
    }

    @Disabled("slow")
    @Test
    fun `fast emitter and slow collector - flowOn`() = runBlocking {
        val emitter = (1..3)
            .asFlow()
            .onEach {
                delay(1000)
                log("Emitted $it")
            }

        println("Exec time = ${measureTimeMillis {
            emitter
                .flowOn(Dispatchers.Default)
                .collect {
                    log("Start collecting $it")
                    delay(3000)
                    log("End collecting $it")
                }
        }
        }")
    }

    @Disabled("slow")
    @Test
    fun `fast emitter and slow collector - buffer`() = runBlocking {
        val emitter = (1..5)
            .asFlow()
            .onEach {
                delay(100)
                log("Emitted $it")
            }

        println("Exec time = ${measureTimeMillis {
            emitter
                .buffer()
                .collect {
                    log("Start collecting $it")
                    delay(300)
                    log("End collecting $it")
                }
        }
        }")
    }

    private fun <T, R> Flow<T>.concurrentMap(
        coroutineContext: CoroutineContext = EmptyCoroutineContext,
        concurrencyLevel: Int = DEFAULT_CONCURRENCY,
        transform: suspend (T) -> R
    ): Flow<R> {
        return flatMapMerge(concurrencyLevel) { value ->
            flow { emit(transform(value)) }
        }.flowOn(coroutineContext)
    }

    @Test
    fun `safgasf df h`() = runBlocking {

        val emitter = (1..100)
            .asFlow()
            .onEach {
                delay(100)
                log("Emitted $it")
            }

        println("Exec time = ${measureTimeMillis {
            emitter
                .concurrentMap(Dispatchers.IO, 100) {
                    log("Start mapping $it")
                    delay(5000)
                    log("End mapping $it")
                    it
                }
                .collect {
                    log("Collected $it")
                }
        }
        }")
    }

    private suspend fun slowDouble(value: Int): Int {
        delay(100)
        return value * 2
    }

    @Test
    fun `dfdfhsd hsd ghsdg`() = runBlocking {
/*
        fun <T> Flow<T>.buffer(size: Int = 0): Flow<T> = flow {
            coroutineScope {
                val channel = produce(capacity = size) {
                    collect { send(it) }
                }
                channel.consumeEach { emit(it) }
            }
        }
*/

        val flow = flow {
            (1..50).forEach {
                delay(100)
                emit(it)
            }
        }

        println("Total exec time: ${measureTimeMillis {
            flow
                .buffer(100)
                .collect {
                    log("Doubling $it")
                    log("Double of $it is ${slowDouble(it)}")
                }

        }}")
    }

    @Test
    fun `sagtas df hdahs`() = runBlocking {

        val flow = (1..15).asFlow()

        println("Total exec time: ${measureTimeMillis {
            flow
                .concurrentMap {
                    log("Doubling $it")
                    log("Double of $it is ${slowDouble(it)}")
                }.collect()

        }}")
    }

    @Test
    fun `asfgsdfh s`() = runBlocking {
        val flow1 = flow {
            for (i in 1..3) {
                //delay(100)
                Thread.sleep(100)
                log("flow1: emiting $i")
                emit(i)
            }
        }.flowOn(Dispatchers.IO)
        val flow2 = flow {
            for (i in 1..3) {
                delay(60)
                log("flow2: emiting $i")
                emit(i)
            }
        }

        val async1 = async {
            flow1.flowOn(Dispatchers.IO).collect {
                log("flow1: collected $it")
            }
        }
        val async2 = async {
            flow2.flowOn(Dispatchers.IO).collect {
                log("flow2: collected $it")
            }
        }

        async1.await()
        async2.await()
    }


    private fun log(message: String) {
        println("${Instant.now()} - $message : ${Thread.currentThread().name}")
    }
}
