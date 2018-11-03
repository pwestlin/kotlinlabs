package se.lantmateriet.taco.kotlin.application

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@WebMvcTest(GreetingController::class)
class GreetingControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var service: GreetingService


    @Test
    fun greet() {
        val name = "Kurt-Sunes med Berit"
        val greeting = "Tjäääna, $name!"

        Mockito.`when`(this.service.greet(name)).thenReturn(greeting)

        this.mvc.perform(get("/greet/{name}", name)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)

    }
}