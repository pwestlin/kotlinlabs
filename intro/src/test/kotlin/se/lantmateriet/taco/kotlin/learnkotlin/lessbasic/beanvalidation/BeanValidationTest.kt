@file:Suppress("RemoveRedundantBackticks", "SpringJavaInjectionPointsAutowiringInspection", "PackageName")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.beanvalidation

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import se.lantmateriet.taco.kotlin.application.KotlinApplication
import se.lantmateriet.taco.kotlin.application.beanvalidation.*
import javax.inject.Inject
import javax.validation.ConstraintViolationException

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [KotlinApplication::class])
class BeanValidationTest {

    @Inject
    lateinit var service: RacingService

    @Test
    fun driver() {
        val kart = Kart(Chassie.ENERGY, Engine.TM_KZ2)
        val driver = Driver("Peter", listOf(kart))

        service.storeDriver(driver)

        assertThat(service.getDriver(driver.name)).isEqualTo(driver)
    }

    @Test
    fun `store driver with too short name`() {
        val kart = Kart(Chassie.ENERGY, Engine.TM_KZ2)
        val driver = Driver("PW", listOf(kart))

        Assertions.assertThatThrownBy { service.storeDriver(driver) }
            .isInstanceOf(ConstraintViolationException::class.java)
            .hasMessageEndingWith("driver.name: size must be between 3 and 20")
    }
}