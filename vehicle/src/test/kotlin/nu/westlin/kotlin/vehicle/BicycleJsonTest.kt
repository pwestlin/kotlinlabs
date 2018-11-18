package nu.westlin.kotlin.vehicle

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BicycleJsonTest {

    val objectMapper = ObjectMapper()

    @Test
    fun `serialize and deserialize car`() {
        val bicycle = Bicycle(id = 1, brand = BicycleBrand.CRESCENT, year = 1967)
        val json = objectMapper.writeValueAsString(bicycle)
        println("json = $json")

        val convertedBicycle = objectMapper.readValue<Bicycle>(json)
        assertThat(convertedBicycle).isEqualTo(bicycle)

        assertThat(objectMapper.writeValueAsString(convertedBicycle)).isEqualTo(json)
    }
}