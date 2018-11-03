@file:Suppress("RemoveRedundantBackticks", "SpringJavaInjectionPointsAutowiringInspection")

package se.lantmateriet.taco.kotlin.application.beanvalidation

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.Size

enum class Chassie(private val fancyName: String) {
    ENERGY("Energy"),
    BIREL("Birel");

    override fun toString(): String {
        return fancyName
    }
}

enum class Engine(private val fancyName: String) {
    TM_KZ2("TM KZ2");

    override fun toString(): String {
        return fancyName
    }
}

data class Kart(val chassie: Chassie, val engine: Engine? = null)
data class Driver(
    @get: Size(min = 3, max = 20) val name: String,
    val karts: List<Kart>
)

@Service
@Validated
class RacingService {
    val driverRepository = mutableMapOf<String, Driver>()

    fun getDriver(name: String?) = driverRepository[name]

    fun storeDriver(@Valid driver: Driver) {
        driverRepository[driver.name] = driver
    }
}

