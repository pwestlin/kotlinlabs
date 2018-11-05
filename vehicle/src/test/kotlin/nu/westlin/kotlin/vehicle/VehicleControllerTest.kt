package nu.westlin.kotlin.vehicle

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import javax.inject.Inject

@RunWith(SpringRunner::class)
@WebMvcTest(VehicleController::class)
class VehicleControllerTest {

    @Inject
    lateinit var mvc: MockMvc

    @Inject
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var carRepository: CarRepository

    @MockBean
    lateinit var bicycleRepository: BicycleRepository

    @Test
    fun `get car by id`() {

        val car = Car(1, CarBrand.PORSCHE, 2018)
        whenever(this.carRepository.get(car.id)).thenReturn(car)

        val mvcResult = this.mvc.perform(MockMvcRequestBuilders.get("/{type}/id/{id}", Type.CAR, car.id)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        assertThat(objectMapper.readValue<Car>(mvcResult.response.contentAsString)).isEqualTo(car)
    }

    @Test
    fun `get all vehicles`() {

        val car = Car(1, CarBrand.PORSCHE, 2018)
        whenever(this.carRepository.all()).thenReturn(listOf(car))

        val bicycle = Bicycle(1, BicycleBrand.MONARK, 2018)
        whenever(this.bicycleRepository.all()).thenReturn(listOf(bicycle))

        val mvcResult = this.mvc.perform(MockMvcRequestBuilders.get("/vehicles")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val vehicles = objectMapper.readValue<List<Vehicle<*>>>(mvcResult.response.contentAsString)

        assertThat(vehicles).containsExactlyInAnyOrder(car, bicycle)
    }

}