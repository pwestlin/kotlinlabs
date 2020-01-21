package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.Instant

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
    fun `flow of the Simpsons in separate coroutine`() = runBlocking {

        theSimpsonsFlow().flowOn(Dispatchers.IO).collect {
            log(it)
        }
    }

    private fun log(message: String) {
        println("${Instant.now()} - $message : ${Thread.currentThread()}")
    }
}