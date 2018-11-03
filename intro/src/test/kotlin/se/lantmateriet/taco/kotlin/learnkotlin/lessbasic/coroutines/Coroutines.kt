@file:Suppress("EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "PackageName")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class CoroutinesTest {


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
        println("Created ${numberOfThreads} threads in ${time}ms.")
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
        println("Created ${numberOfCoroutines} coroutines in ${time}ms.")
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

            println("The answer is: ${value1} ${value2} ${value3}")
        }
        println("Exec time: $time ms")
    }

    @Test
    fun `call three external services in parallel (with coroutines) and aggregate the result`() {
        val time = measureTimeMillis {
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
    fun `foo`() {
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

    suspend fun slowAsATurtle(time: Long) {
        delay(time)
    }
}