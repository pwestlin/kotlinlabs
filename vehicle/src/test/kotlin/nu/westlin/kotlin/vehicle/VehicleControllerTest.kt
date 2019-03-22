package nu.westlin.kotlin.vehicle

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
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
            .accept(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        assertThat(objectMapper.readValue<Car>(mvcResult.response.contentAsString)).isEqualTo(car)
    }

    /**
     * HTTPie: http POST http://localhost:8080/Car id='-1' type='CAR' year=1988 brand='VOLVO'
     */
    @Test
    fun `add car`() {
        val car = Car(-1, CarBrand.PORSCHE, 2018)
        val createdCar = car.copy(id = 3)
        whenever(this.carRepository.add(car)).thenReturn(createdCar)

        val mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/Car")
            .content(objectMapper.writeValueAsString(car))
            .contentType(APPLICATION_JSON_UTF8)
            .accept(APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        assertThat(objectMapper.readValue<Car>(mvcResult.response.contentAsString)).isEqualTo(createdCar)
    }

    /**
     * HTTPie: http POST http://localhost:8080/Vehicle id='-1' type='BICYCLE' year=1988 brand='MONARK'
     */
    @Test
    fun `add vehicle`() {
        val bicycle = Bicycle(-1, BicycleBrand.CRESCENT, 2018)
        val createdBicycle= bicycle.copy(id = 3)
        whenever(this.bicycleRepository.add(bicycle)).thenReturn(createdBicycle)

        val mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/Vehicle")
            .content(objectMapper.writeValueAsString(bicycle))
            .contentType(APPLICATION_JSON_UTF8)
            .accept(APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        assertThat(objectMapper.readValue<Bicycle>(mvcResult.response.contentAsString)).isEqualTo(createdBicycle)
    }

    @Test
    fun `get all vehicles`() {
        val car1 = Car(1, CarBrand.PORSCHE, 2018)
        val car2 = Car(2, CarBrand.VOLVO, 1972)
        whenever(this.carRepository.all()).thenReturn(listOf(car1, car2))

        val bicycle = Bicycle(1, BicycleBrand.MONARK, 2018)
        whenever(this.bicycleRepository.all()).thenReturn(listOf(bicycle))

        val mvcResult = this.mvc.perform(MockMvcRequestBuilders.get("/vehicles")
            .accept(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val vehicles = objectMapper.readValue<List<Vehicle<*>>>(mvcResult.response.contentAsString)

        assertThat(vehicles).containsExactlyInAnyOrder(car1, car2, bicycle)
    }

}