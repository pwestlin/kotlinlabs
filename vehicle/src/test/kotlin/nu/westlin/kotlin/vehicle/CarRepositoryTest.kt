package nu.westlin.kotlin.vehicle

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CarRepositoryTest {


    val repository = CarRepository()

    @Test
    fun `find all cars`() {
        assertThat(repository.all()).containsExactlyInAnyOrderElementsOf(repository.cars)
    }

    @Test
    fun `find a car by id`() {
        val vehicle = repository.cars.last()
        assertThat(repository.get(vehicle.id)).isEqualTo(vehicle)
    }

    @Test
    fun `add car`() {
        val car = Car(-1, CarBrand.PORSCHE, 1981)
        val createdCar = repository.add(car)
        assertThat(repository.cars).contains(createdCar)
    }
}