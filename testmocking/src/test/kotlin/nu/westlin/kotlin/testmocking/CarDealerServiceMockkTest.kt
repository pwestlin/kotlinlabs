package nu.westlin.kotlin.testmocking

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CarDealerServiceMockkTest {

    val repository = mockk<CarRepository>(relaxed = true)

    val service = CarDealerService(repository)

    @Test
    fun `sell car`() {
        val car = VOLVO_2016

        service.sell(car)

        verify(exactly = 1) { repository.add(car) }
    }

    @Test
    fun `find car by brand and production year`() {
        val car = PORSCHE_2018

        every { repository.find(allAny<(Car) -> Boolean>()) } returns listOf(car)

        assertThat(service.findByBrandAndProductionYear(car.brand, car.productionYear)).containsExactly(car)
    }
}