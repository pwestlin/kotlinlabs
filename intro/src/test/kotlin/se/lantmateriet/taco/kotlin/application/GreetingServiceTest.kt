package se.lantmateriet.taco.kotlin.application

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GreetingServiceTest {

    @Mock
    lateinit var repository: GreetingRepository

    lateinit var service: GreetingService

    @Before
    fun initMockitoAnnotations() {
        MockitoAnnotations.initMocks(this)
        service = GreetingService(repository)
    }

    @Test
    fun greet() {
        val greet = "Guten tag"
        Mockito.`when`(repository.greeting()).thenReturn(greet)
        val name = "Sune"
        assertThat(service.greet(name)).isEqualTo("$greet, $name!")
    }
}