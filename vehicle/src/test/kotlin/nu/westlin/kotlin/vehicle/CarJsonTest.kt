package nu.westlin.kotlin.vehicle

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CarJsonTest {

    val objectMapper = ObjectMapper()

    @Test
    fun `serialize and deserialize car`() {
        val car = Car(id = 1, brand = CarBrand.PORSCHE, year = 1967)
        val json = objectMapper.writeValueAsString(car)
        println("json = $json")

        val convertedCar = objectMapper.readValue<Car>(json)

        assertThat(objectMapper.writeValueAsString(convertedCar)).isEqualTo(json)
    }
}