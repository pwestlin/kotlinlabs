package nu.westlin.kotlin.testmocking

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CarDealerServicePureMockitoTest {

    @Mock
    lateinit var repository: CarRepository

    lateinit var service: CarDealerService

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        service = CarDealerService(repository)
    }

    @Test
    fun `sell car`() {
        val car = VOLVO_2016

        /*
        Här kommer man få fel eftersom CarRepository är final (klasser i Kotlin är final som default).
        För att lösa det kan man lägga till en dependency: testImplementation ‘org.mockito:mockito-inline:2.23.0’
         */
        service.sell(car)

        Mockito.verify(repository).add(car)
    }

    @Test
    fun `find car by brand and production year`() {
        val car = PORSCHE_2018

        // Ingen av dessa nedan funkar... :/
        //Mockito.`when`(repository.find(any<Predicate<(Car) -> Boolean>>())).thenReturn(listOf(car))
        //Mockito.`when`(repository.find(any<Any>())).thenReturn(listOf(car))
        //Mockito.`when`(repository.find { any() }).thenReturn(listOf(car))
        //Mockito.`when`(repository.find(any<(Car) -> Boolean>())).thenReturn(listOf(car))

        //assertThat(service.findByBrandAndProductionYear(car.brand, car.productionYear)).isEqualTo(car)
    }
}