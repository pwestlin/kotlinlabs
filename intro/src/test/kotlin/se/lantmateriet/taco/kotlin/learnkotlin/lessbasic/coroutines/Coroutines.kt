@file:Suppress("EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "PackageName")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class CoroutinesTest {

    fun slowSync(execTime: Long): Long {
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
        println("Sum of exec times: ${sumOfExecTimes * 1000}")
        println("Exec time sync: $time")
    }

    suspend fun slowAsync(execTime: Long): Long {
        println("slowAsync: ${Thread.currentThread()}")
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

        println("Exec time sync: $time")
    }

    @Test
    fun `get three values in async with return values`() {
        val execTimes = mutableListOf<Deferred<Long>>()
        val time = measureTimeMillis {
            runBlocking {
                execTimes.add(async { slowAsync(3) })
                execTimes.add(async { slowAsync(2) })
                execTimes.add(async { slowAsync(1) })
                println("Sum of exec times: ${execTimes.sumBy { it.await().toInt() } * 1000}")
            }
        }

        println("Exec time sync: $time")
    }

    @Test
    fun `get three values in async in different threads with return values`() {
        val execTimes = mutableListOf<Deferred<Long>>()
        val time = measureTimeMillis {
            runBlocking {
                execTimes.add(async(Dispatchers.Default) { slowAsync(3) })
                execTimes.add(async(Dispatchers.Default) { slowAsync(2) })
                execTimes.add(async(Dispatchers.Default) { slowAsync(1) })
                println("Sum of exec times: ${execTimes.sumBy { it.await().toInt() } * 1000}")
            }
        }

        println("Exec time sync: $time")
    }

    @Test
    fun `Hello world! `() {
        runBlocking {
            // this: CoroutineScope
            launch {
                // launch new coroutine in the scope of runBlocking
                delay(1000L)
                println("World!")
            }
            println("Hello,")
        }
    }

    @Test
    fun `order of execution `() {
        runBlocking {
            // this: CoroutineScope
            launch {
                delay(200L)
                println("Task from runBlocking")
            }

            // coroutinescope stops current ("parent") coroutine's exectuion
            coroutineScope {
                // Creates a new coroutine scope
                launch {
                    delay(500L)
                    println("Task from nested launch")
                }

                delay(100L)
                println("Task from coroutine scope") // This line will be printed before nested launch
            }

            println("Coroutine scope is over") // This line is not printed until nested launch completes
        }
    }

    @Test
    fun `test launch`() {
        fun doSomething() {
            println("1: ${Thread.currentThread()}")
        }


        println(Thread.currentThread())
        runBlocking {
            println("2: ${Thread.currentThread()}")
            launch {
                println("3: ${Thread.currentThread()}")
                doSomething()
                println("4: ${Thread.currentThread()}")
            }
            println("5: ${Thread.currentThread()}")
        }
        println("6: ${Thread.currentThread()}")
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
        println("Created $numberOfThreads threads in ${time}ms.")
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
        println("Created $numberOfCoroutines coroutines in ${time}ms.")
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

            println("The answer is: $value1 $value2 $value3")
        }
        println("Exec time: $time ms")
    }

    @Test
    fun `call three external services in parallel (with coroutines) and aggregate the result`() {
        val time = measureTimeMillis {
            // GlobalScope.async starts a "top-level coroutine"
            val value1 = GlobalScope.async { service1() }
            val value2 = GlobalScope.async { service2() }
            val value3 = GlobalScope.async { service3() }
            runBlocking {
                println("The answer is: ${value1.await()} ${value2.await()} ${value3.await()}")
            }
        }
        println("Exec time: $time ms")
    }

    @Test
    fun `call three external services in parallel (with coroutines) and aggregate the result 2`() {
        val time = measureTimeMillis {
            // Dispatchers.Default -> run on threads from default thread pool
            runBlocking(Dispatchers.Default) {
                // async starts a coroutine in current coroutine context (which comes from runBlocking in this case)
                val value1 = async { service1() }
                val value2 = async { service2() }
                val value3 = async { service3() }
                println("The answer is: ${value1.await()} ${value2.await()} ${value3.await()}")
            }
        }
        println("Exec time: $time ms")
    }

    @Test
    fun `foo bar`() {
        runBlocking {
            // this: CoroutineScope
            launch {
                // launch new coroutine in the scope of runBlocking
                delay(1000L)
                print("World!")
            }
            launch {
                // launch new coroutine in the scope of runBlocking
                delay(750L)
                print(", ")
            }
            launch {
                // launch new coroutine in the scope of runBlocking
                delay(500L)
                print("Hello")
            }
        }
    }

    fun service1(): String {
        doSillyStuff()
        return "Finns"
    }

    fun service2(): String {
        doSillyStuff()
        return "i"
    }

    fun service3(): String {
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
                        print(".")
                    }
                }
            }
        }
        println("\ntime = $time ms")
    }

    @Test
    fun `atest withContext`() {
        runBlocking {
            launch { slowWork(1) }
        }
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
            println("$times execs with delay of $delay each took $this millis")
        }
    }

    suspend fun slowWork(jobId: Int, delay: Long = 1000) = withContext(Dispatchers.Default) {
        delay(delay)
        println("${System.currentTimeMillis().toShort()} Job $jobId done!")
    }

    suspend fun slowAsATurtle(time: Long) {
        delay(time)
    }
}