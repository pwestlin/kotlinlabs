package nu.westlin.kotlin.vehicle

import nu.westlin.kotlin.vehicle.Type.CAR
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class VehicleRepositoryTest {


    val repository = VehicleRepository()

    @Test
    fun `find all cars`() {
        assertThat(repository.all()).containsExactlyInAnyOrderElementsOf(repository.vehicles)
    }

    @Test
    fun `find a car by id`() {
        val vehicle = repository.vehicles.last()
        assertThat(repository.get(vehicle.id)).isEqualTo(vehicle)
    }

    @Test
    fun `find cars by type`() {
        assertThat(repository.findBy(CAR)).containsExactlyInAnyOrder(repository.vehicles[0], repository.vehicles[1], repository.vehicles[2])
    }

    @Test
    fun `find cars by class type`() {
        assertThat(repository.findBy(Car::class)).containsExactlyInAnyOrder(repository.vehicles[0], repository.vehicles[1], repository.vehicles[2])
    }

    @Test
    fun `add vehicle`() {
        val bicycle = Bicycle(5, Brand.MONARK, 1981)
        repository.add(bicycle)
        assertThat(repository.vehicles).contains(bicycle)
    }
}