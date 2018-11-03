package se.lantmateriet.taco.kotlin.application

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class GreetingRepositoryTest() {
    val repository: GreetingRepository = GreetingRepository()

    @Test
    fun callIt() {
        assertThat(repository.greeting()).isIn("Hi", "Yo", "Hello")
    }
}