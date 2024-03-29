@file:Suppress(
    "EXPERIMENTAL_FEATURE_WARNING",
    "MemberVisibilityCanBePrivate",
    "PackageName",
    "TestFunctionName",
    "NonAsciiCharacters",
    "SameParameterValue"
)

package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

@ExperimentalCoroutinesApi
@Suppress("RemoveRedundantBackticks", "UNUSED_VARIABLE")
@ObsoleteCoroutinesApi
class CoroutinesTest {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun slowSync(execTime: Long): Long {
        logger.info("slowSync")
        Thread.sleep(execTime * 1000)
        return execTime
    }

    @Test
    fun `get lots in sync`() {
        var sumOfExecTimes = 0L
        val time = measureTimeMillis {
            sumOfExecTimes += slowSync(3)
            sumOfExecTimes += slowSync(2)
            sumOfExecTimes += slowSync(1)
        }
        log("Sum of exec times: ${sumOfExecTimes * 1000}")
        log("Exec time sync: $time")
    }

    suspend fun slowAsync(execTime: Long): Long {
        log("slowAsync")
        delay(execTime * 1000)
        return execTime
    }

    @Test
    fun `get three values in async`() {
        val time = measureTimeMillis {
            runBlocking {
                val a = launch { slowAsync(3) }
                launch { slowAsync(2) }
                launch { slowAsync(1) }
                a.join()
            }
        }

        log("Exec time sync: $time")
    }

    @Test
    fun `get three values in async with return values`() {
        val execTimes = mutableListOf<Deferred<Long>>()
        val time = measureTimeMillis {
            runBlocking {
                execTimes.add(async { slowAsync(3) })
                execTimes.add(async { slowAsync(2) })
                execTimes.add(async { slowAsync(1) })
                log("Sum of exec times: ${execTimes.sumOf { it.await().toInt() } * 1000}")
            }
        }

        log("Exec time sync: $time")
    }

    @Test
    fun `get three values in async in different threads with return values`() {
        val execTimes = mutableListOf<Deferred<Long>>()
        val time = measureTimeMillis {
            runBlocking {
                execTimes.add(async(Dispatchers.Default) { slowAsync(3) })
                execTimes.add(async(Dispatchers.Default) { slowAsync(2) })
                execTimes.add(async(Dispatchers.Default) { slowAsync(1) })
                log("Sum of exec times: ${execTimes.sumOf { it.await().toInt() } * 1000}")
            }
        }

        log("Exec time sync: $time")
    }

    @Test
    fun `Hello world! `() {
        runBlocking {
            // this: CoroutineScope
            launch {
                // launch new coroutine in the scope of runBlocking
                delay(1000L)
                log("World!")
            }
            log("Hello,")
        }
    }

    @Test
    fun `order of execution `() {
        runBlocking {
            // this: CoroutineScope
            launch {
                delay(200L)
                log("Task from runBlocking")
            }

            // coroutinescope stops current ("parent") coroutine's exectuion
            coroutineScope {
                // Creates a new coroutine scope
                launch {
                    delay(500L)
                    log("Task from nested launch")
                }

                delay(100L)
                log("Task from coroutine scope") // This line will be printed before nested launch
            }

            log("Coroutine scope is over") // This line is not printed until nested launch completes
        }
    }

    @Test
    fun `test launch`() {
        fun doSomething() {
            log("1")
        }


        runBlocking {
            log("2")
            launch {
                log("3")
                doSomething()
                log("4")
            }
            log("5")
        }
        log("6")
    }


    // Introduktion till coroutines: https://proandroiddev.com/async-operations-with-kotlin-coroutines-part-1-c51cc581ad33
    // Bl.a. launch vs async: https://proandroiddev.com/coroutines-snags-6bf6fb53a3d1

    @Test
    fun `a bunch of threads`() {
        var counter = 0
        val numberOfThreads = 100_000
        val time = measureTimeMillis {
            repeat(numberOfThreads) {
                thread {
                    counter += 1
                }
            }
        }
        log("Created $numberOfThreads threads in ${time}ms.")
    }

    @Test
    fun `a bunch of coroutines`() {
        var counter = 0
        val numberOfCoroutines = 100_000
        val time = measureTimeMillis {
            runBlocking {
                repeat(numberOfCoroutines) {
                    // launch a lot of coroutines
                    launch {
                        counter += 1
                    }
                }
            }
        }
        log("Created $numberOfCoroutines coroutines in ${time}ms.")
    }

    @Test
    fun `sekventiell exekvering av hårt internt arbete`() {
        var counter = 0
        val numberOfCoroutines = 5
        val time = measureTimeMillis {
            runBlocking {
                repeat(numberOfCoroutines) {
                    // launch a lot of coroutines
                    launch {
                        log("Exekverar coroutine #$counter...")
                        simuleraHårtInterntArbete(1000)
                        log("Coroutine #$counter klar!")
                        counter += 1
                    }
                }
            }
        }
        log("Created $numberOfCoroutines coroutines in ${time}ms.")
    }

    @Test
    fun `exekvering på flera trådar av hårt internt arbete`() {
        val counter = AtomicInteger()
        val numberOfCoroutines = 5
        val time = measureTimeMillis {
            runBlocking(Dispatchers.Default) {
                repeat(numberOfCoroutines) {
                    launch {
                        val internalCounter = counter.getAndIncrement()
                        log("Exekverar coroutine #$internalCounter...")
                        simuleraHårtInterntArbete(1000)
                        log("Coroutine #$internalCounter klar!")

                    }
                }
            }
        }
        log("Created $numberOfCoroutines coroutines in ${time}ms.")
    }

    @Test
    fun `sekventiell exekvering av hårt externt arbete`() {
        val counter = AtomicInteger()
        val numberOfCoroutines = 5
        val time = measureTimeMillis {
            runBlocking {
                repeat(numberOfCoroutines) {
                    launch {
                        val internalCounter = counter.getAndIncrement()
                        log("Exekverar coroutine #$internalCounter...")
                        simuleraHårtExterntArbete(1000)
                        log("Coroutine #$internalCounter klar!")
                    }
                }
            }
        }
        log("Created $numberOfCoroutines coroutines in ${time}ms.")
    }

    @Test
    fun `parallell exekvering av hårt externt arbete`() {
        val counter = AtomicInteger()
        val numberOfCoroutines = 5
        val time = measureTimeMillis {
            runBlocking(Dispatchers.IO) {
                repeat(numberOfCoroutines) {
                    launch {
                        val internalCounter = counter.getAndIncrement()
                        log("Exekverar coroutine #$internalCounter...")
                        simuleraHårtExterntArbete(1000)
                        log("Coroutine #$internalCounter klar!")
                    }
                }
            }
        }
        log("Created $numberOfCoroutines coroutines in ${time}ms.")
    }

    @Test
    fun `structured concurrency - 1`() {
        runBlocking {
            launch {
                delay(300)
                println("best!")
            }
            launch {
                delay(200)
                println("the")
            }
            launch {
                delay(100)
                println("are")
            }
            println("Tacos")
        }
    }

    @Suppress(
        "IMPLICIT_NOTHING_AS_TYPE_PARAMETER",
        "UNREACHABLE_CODE",
        "IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION"
    )
    @Test
    fun `felhantering - 1`() {
        runBlocking {
            val job = launch {
                println("Throwing exception from launch")
                throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
            }
            job.join()
            println("Joined failed job")
            val deferred = GlobalScope.async {
                println("Throwing exception from async")
                throw ArithmeticException() // Nothing is printed, relying on user to call await
            }
            try {
                deferred.await()
                println("Unreached")
            } catch (e: ArithmeticException) {
                println("Caught ArithmeticException")
            }
        }
    }

    @Test
    fun `felhantering`() {
        runBlocking {
            launch {
                log("Än så länge är jag glad men vänta du bara...")
                delay(100)
                throw IndexOutOfBoundsException("Jag är skitoglad!")
            }
            launch {
                log("Jag är glad!")
                delay(200)
                log("Detta kommer aldrig att exekveras.")
            }
            launch {
                log("Jag är också glad!")
            }
        }
    }

    @Test
    fun `returnera värde`() {
        runBlocking {
            val deferred = async { 5 }
            println("deferred = $deferred")
            println("deferred.await() = ${deferred.await()}")
        }
    }

    @Test
    fun `en lista av värden`() {
        runBlocking {
            val sum = listOf(1, 2, 3, 4, 5)
                .map { index -> async { index } }
                .map { it.await() }
                .sum()
            println("sum = $sum")
        }
    }


    private fun simuleraHårtInterntArbete(millis: Long) {
        Thread.sleep(millis)
    }

    private suspend fun simuleraHårtExterntArbete(millis: Long) {
        delay(millis)
    }

    /*
        The magic here is that coroutines execute on a shared pool of threads and since one thread can run many coroutines,
        there isn’t a need to spawn a new thread for every block of code that’s being executed.
     */


    @Test
    fun `call three external services in sequence and aggregate the result`() {
        val time = measureTimeMillis {
            val value1 = service1()
            val value2 = service2()
            val value3 = service3()

            log("The answer is: $value1 $value2 $value3")
        }
        log("Exec time: $time ms")
    }

    @Test
    fun `call three external services in parallel with coroutines and aggregate the result`() {
        val time = measureTimeMillis {
            // GlobalScope.async starts a "top-level coroutine"
            val value1 = GlobalScope.async { service1() }
            val value2 = GlobalScope.async { service2() }
            val value3 = GlobalScope.async { service3() }
            runBlocking {
                log("The answer is: ${value1.await()} ${value2.await()} ${value3.await()}")
            }
        }
        log("Exec time: $time ms")
    }

    @Test
    fun `call three external services in parallel with coroutines and aggregate the result 2`() {
        val time = measureTimeMillis {
            // Dispatchers.Default -> run on threads from default thread pool
            runBlocking(Dispatchers.Default) {
                // async starts a coroutine in current coroutine context (which comes from runBlocking in this case)
                val value1 = async { service1() }
                val value2 = async { service2() }
                val value3 = async { service3() }
                log("The answer is: ${value1.await()} ${value2.await()} ${value3.await()}")
            }
        }
        log("Exec time: $time ms")
    }

    @Test
    fun `foo bar`() {
        runBlocking {
            // this: CoroutineScope
            launch {
                // launch new coroutine in the scope of runBlocking
                delay(1000L)
                log("World!")
            }
            launch {
                // launch new coroutine in the scope of runBlocking
                delay(750L)
                log(", ")
            }
            launch {
                // launch new coroutine in the scope of runBlocking
                delay(500L)
                log("Hello")
            }
        }
    }

    fun service1(): String {
        logger.info("service1")
        doSillyStuff()
        return "Finns"
    }

    fun service2(): String {
        logger.info("service2")
        doSillyStuff()
        return "i"
    }

    fun service3(): String {
        logger.info("service3")
        doSillyStuff()
        return "sjön"
    }

    fun doSillyStuff() {
        var counter = 0
        for (i in 1..5_000_000_000) {
            counter += 1
        }
    }

    @Test
    fun coroutines() {
        val time = measureTimeMillis {
            runBlocking {
                repeat(100_000) {
                    // launch a lot of coroutines
                    launch {
                        slowAsATurtle(1000L)
                        log(".")
                    }
                }
            }
        }
        log("\ntime = $time ms")
    }

    @Test
    fun `atest withContext with async`() = runBlocking<Unit> {
        withContext(Dispatchers.Default) {
            slowWork(1)
        }
    }

    @Test
    fun `atest withContext with sync`() = runBlocking<Unit> {
        withContext(Dispatchers.Default) {
            slowSync(1)
        }
        withContext(Dispatchers.Default) {
            slowSync(1)
        }
    }

    @Test
    fun `atest withContext with sync 2`() = runBlocking<Unit> {
        suspend fun doWithContext() {
            withContext(Dispatchers.Default) {
                slowSync(1)
            }
        }

        logger.info("Start")
        doWithContext()
        doWithContext()
        logger.info("End")
    }

    @Test
    fun `repeat slow work`() {
        val times = 5
        val delay = 1000
        with(measureTimeMillis {
            runBlocking {
                repeat(times) {
                    launch { slowWork(delay) }
                }
            }
        }) {
            log("$times execs with delay of $delay each took $this millis")
        }
    }

    @Test
    fun `try Dispatchers`() = runBlocking<Unit> {
        launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) { // not confined -- will work with main thread
            println("Default         : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            println("Default         : After delay in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.IO) { // not confined -- will work with main thread
            println("IO              : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            println("IO              : After delay in thread ${Thread.currentThread().name}")
        }
        launch(newFixedThreadPoolContext(10, "Foo")) { // context of the parent, main runBlocking coroutine
            println("Fixed pool      : I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            println("Fixed pool      : After delay in thread ${Thread.currentThread().name}")
        }
        launch { // context of the parent, main runBlocking coroutine
            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
        }
    }

    @Test
    fun `jump between threads`() {
        newSingleThreadContext("Ctx1").use { ctx1 ->
            newSingleThreadContext("Ctx2").use { ctx2 ->
                runBlocking(ctx1) {
                    log("Started in ctx1")
                    withContext(ctx2) {
                        log("Working in ctx2")
                    }
                    log("Back to ctx1")
                }
            }
        }
    }

    @Test
    fun `try newFixedThreadPoolContext`() {
        val dispatcher = newFixedThreadPoolContext(1, "fisk")
        val execTime = measureTimeMillis {
            runBlocking {
                repeat(1000) {
                    launch(dispatcher) {
                        println("hej - ${Thread.currentThread().name}")
                        delay(500)
                    }
                }
            }
        }

        println("execTime = $execTime")
    }

    suspend fun slowWork(jobId: Int, delay: Long = 1000) = withContext(Dispatchers.Default) {
        delay(delay)
        log("${System.currentTimeMillis().toShort()} Job $jobId done!")
    }

    suspend fun slowAsATurtle(time: Long) {
        delay(time)
    }

    @Test
    fun `test delay in launch with virtual time by runBlockingTest`() = runBlockingTest {
        suspend fun bar() = coroutineScope {
            launch {
                delay(1000)     // auto-advances without delay
                println("bar")  // executes eagerly when bar() is called
            }
        }

        val realStartTime = System.currentTimeMillis()
        val virtualStartTime = currentTime

        bar()

        println("${System.currentTimeMillis() - realStartTime} ms")  // ~ 11 ms
        println("${currentTime - virtualStartTime} ms")              // 1000 ms
    }

    @Test
    fun `custom CoroutineDispatcher`() = runBlocking {
        val dispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
        val semaphore = Semaphore(8)

        val execTime = measureTimeMillis {
            val list = (1..8).toList().map { value ->
                async(dispatcher) {
                    semaphore.withPermit {
                        log("start")
                        delay(1000)
                        val v = value * 2
                        log("end")
                        v
                    }
                }
            }.awaitAll()

            log(list)
        }
        log("execTime = $execTime ms")
    }

    @Test
    fun `split into chunks`() = runBlocking {
        val dispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

        val execTime = measureTimeMillis {
            val list = (1..8).toList().chunked(8).map { list ->
                list.map { value ->
                    async {
                        log("start")
                        delay(1000)
                        val v = value * 2
                        log("end")
                        v
                    }
                }
            }
        }
    }
}

fun log(msg: String) {
    println("${Instant.now()}: $msg - ${Thread.currentThread().name}")
}