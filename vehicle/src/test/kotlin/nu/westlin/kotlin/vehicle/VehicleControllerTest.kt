package nu.westlin.kotlin.vehicle

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import javax.inject.Inject

@RunWith(SpringRunner::class)
@WebMvcTest(VehicleController::class)
class VehicleControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Inject
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var repository: VehicleRepository

    @Test
    fun getById() {

        val vehicle = Car(1, Brand.PORSCHE, 2018)
        whenever(this.repository.get(vehicle.id)).thenReturn(vehicle)

        this.mvc.perform(MockMvcRequestBuilders.get("/vehicle/{id}", vehicle.id)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(vehicle)))
    }

}