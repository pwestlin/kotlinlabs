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
        val car = Car(5, Brand.PORSCHE, 1981)
        repository.add(car)
        assertThat(repository.cars).contains(car)
    }
}