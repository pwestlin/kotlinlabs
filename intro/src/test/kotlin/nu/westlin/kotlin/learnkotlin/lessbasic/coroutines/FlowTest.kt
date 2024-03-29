package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.net.URI
import java.time.Instant
import java.util.Locale
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
                emit(it.uppercase(Locale.getDefault()))
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
            .map { it.uppercase(Locale.getDefault()).also { _ -> log(it) } }
            .flowOn(Dispatchers.Default)
            .filter { it.startsWith("M").also { _ -> log(it) } }
            .flowOn(Dispatchers.Unconfined)
            .collect { log(it) }
    }

    @Test
    fun `flow of the Simpsons on separate threads and coroutines`() = runBlocking {
        fun toUppercase(value: String): String = value.uppercase(Locale.getDefault()).also { log(it) }
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

        println("Exec time = ${
            measureTimeMillis {
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

        println("Exec time = ${
            measureTimeMillis {
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

        println("Exec time = ${
            measureTimeMillis {
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

        println("Exec time = ${
            measureTimeMillis {
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

        println("Total exec time: ${
            measureTimeMillis {
                flow
                    .buffer(100)
                    .collect {
                        log("Doubling $it")
                        log("Double of $it is ${slowDouble(it)}")
                    }

            }
        }")
    }

    @Test
    fun `sagtas df hdahs`() = runBlocking {

        val flow = (1..15).asFlow()

        println("Total exec time: ${
            measureTimeMillis {
                flow
                    .concurrentMap {
                        log("Doubling $it")
                        log("Double of $it is ${slowDouble(it)}")
                    }.collect()

            }
        }")
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

@ExperimentalCoroutinesApi
internal class AntoherFlowTest {

    private fun currTime() = System.currentTimeMillis()
    var start: Long = 0

    private fun emitter(): Flow<Int> =
        (1..5)
            .asFlow()
            .onStart { start = currTime() }
            .onEach {
                delay(100)
                print("Emit $it (${currTime() - start}ms) ")
            }

    @Test
    fun `collect stuff`() = runBlocking {
        val time = measureTimeMillis {
            emitter()
                //.flowOn(Dispatchers.Default)
                .buffer()
                .collect {
                    print("\nCollect $it starts (${currTime() - start}ms) ")
                    delay(300)
                    println("Collect $it ends (${currTime() - start}ms) ")
                }
        }
        print("\nCollected in $time ms")
    }
}

@ExperimentalCoroutinesApi
internal class YetAnotherFlowTest {

    private fun createFlow(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            log("emiting $i")
            emit(i)
        }
    }

    @Test
    fun `test flow`() = runBlocking {
        val flow = createFlow()
        withContext(Dispatchers.IO) {
            flow.collect {
                log("$it")
            }
        }
    }
}

@ExperimentalCoroutinesApi
internal class FastEmiterSlowConsumerFlowTest {

    private fun createFlow(number: Int = 32): Flow<Int> = flow {
        for (i in 1..number) {
            delay(10)
            log("emit $i")
            emit(i)
        }
    }

    @Test
    fun `sfgasfg fea sdg`() = runBlocking {
        val execTime = measureTimeMillis {
            createFlow(4)
/*
                .filter {
                    log("filter $it")
                    it % 2 == 0
                }
*/
                .buffer(8)
                .map {
                    delay(100)
                    log("map $it")
                    it * 2
                }
                //.buffer(8)
                .flowOn(Dispatchers.Default)
                .collect {
                    log("collect $it")
                }
        }

        log("execTime: $execTime ms")
    }

    @Test
    fun `sfgasfg fea sdg 2`() = runBlocking {
        val execTime = measureTimeMillis {
            val result: ArrayList<Deferred<Int>> = ArrayList()
            createFlow(4)
                .buffer(8)
                .collect {
                    result.add(async {
                        delay(100)
                        log("map $it")
                        it * 2
                    })
                }
            result.awaitAll()
        }

        log("execTime: $execTime ms")
    }

    @Test
    fun `test flow`() = runBlocking {
        val flow = createFlow()
/*
        withContext(Dispatchers.IO) {
            flow.collect {
                log("$it")
            }
        }
*/
        val execTime = measureTimeMillis {
            //coroutineScope {
            withContext(Dispatchers.IO) {
                flow
                    .buffer(8)
                    .collect {
                        launch/*(Dispatchers.Default)*/ {
                            delay(100)
                            log("Collected $it")
                        }
                    }
            }
        }
        log("execTime: $execTime ms")
        //assertThat(execTime).isBetween()
    }

    @Test
    fun `satgdafyh sdfh `() {
        @Suppress("RedundantNullableReturnType")
        val foo: List<Map<URI, String>>? = listOf(
            mapOf(
                URI("foo1") to "foo1"
            )
            , mapOf(
                URI("foo2") to "foo2"
            )
            , mapOf(
                URI("foo3") to "foo3"
            )
            , mapOf(
                URI("foo4") to "foo4"
            )
        )

        val a = foo?.flatMap { it.entries }
                    ?.groupBy({ it.key }, { it.value })
                    ?.mapValues { it.value.first() }
        println(a)
        val b = foo?.map { it.entries.first().toPair() }?.toMap()
        println(b)


        //assertThat(a).isEqualTo(b)

        println("${foo?.fold(mapOf<URI,String>()) { acc, map -> acc + map }}")
    }
}