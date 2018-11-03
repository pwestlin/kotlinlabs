package nu.westlin.kotlin.testmocking

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CarDealerServiceMockitoKotlinTest {

    val repository: CarRepository = mock()

    val service: CarDealerService = CarDealerService(repository)

    @Test
    fun `sell car`() {
        val car = VOLVO_2016

        /*
        Här kommer man få fel eftersom CarRepository är final (klasser i Kotlin är final som default).
        För att lösa det kan man lägga till en dependency: testImplementation ‘org.mockito:mockito-inline:2.23.0’
         */
        service.sell(car)

        verify(repository).add(car)
    }

    @Test
    fun `find car by brand and production year`() {
        val car = PORSCHE_2018
        whenever(repository.find(any<(Car) -> Boolean>())).thenReturn(listOf(car))

        assertThat(service.findByBrandAndProductionYear(car.brand, car.productionYear)).containsExactly(car)
    }
}