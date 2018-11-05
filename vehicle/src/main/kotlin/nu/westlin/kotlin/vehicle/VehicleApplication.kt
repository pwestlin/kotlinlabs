@file:Suppress("unused")

package nu.westlin.kotlin.vehicle

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import nu.westlin.kotlin.vehicle.CarBrand.*
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletResponse


@SpringBootApplication
class DatabaseApplication

fun main(args: Array<String>) {
    runApplication<DatabaseApplication>(*args)
}


@RestController
@RequestMapping("/")
class VehicleController(private val carRepository: CarRepository, private val bicycleRepository: BicycleRepository) {

    @GetMapping("/Type/{type}")
    fun getByType(@PathVariable type: Type, response: HttpServletResponse): List<Vehicle<*>> {
        return when (type) {
            Type.CAR -> carRepository.all()
            Type.BICYCLE -> bicycleRepository.all()
        }
    }

    // TODO petves: CAR -> Car
    @GetMapping("/{type}/id/{id}")
    fun getByTypeAndId(@PathVariable type: Type, @PathVariable id: Int, response: HttpServletResponse): Vehicle<*>? {
        val vehicle = when (type) {
            Type.CAR -> carRepository.get(id)
            Type.BICYCLE -> bicycleRepository.get(id)
        }
        if (vehicle == null) response.status = HttpStatus.NOT_FOUND.value()
        return vehicle
    }

    @GetMapping("cars")
    fun getAllCars() = carRepository.all()

    @GetMapping("vehicles")
    fun getAllVehicles(): List<Vehicle<*>> {
        return carRepository.all().union(bicycleRepository.all()).toList()
    }

}

@Component
class TypeEnumConverter : Converter<String, Type> {
    private val logger = LoggerFactory.getLogger(this.javaClass)!!

    override fun convert(source: String): Type {
        try {
            return Type.valueOf(source.toUpperCase())
        } catch (e: IllegalArgumentException) {
            logger.error("Type '$source' is not a valid type", e)
            throw e
        }
    }
}

interface VehicleRepository<T : Vehicle<T>> {
    fun all(): List<T>
    fun add(vehicle: T)
    fun get(id: Int): T?
}

@Repository
class CarRepository : VehicleRepository<Car> {
    val cars = mutableListOf(
        Car(id = 1, brand = VOLVO, year = 1987),
        Car(id = 2, brand = PORSCHE, year = 1962),
        Car(id = 3, brand = RELIANT_ROBIN, year = 1993, noWheels = 3)
    )

    override fun all() = cars.toList()

    override fun add(vehicle: Car) {
        cars.add(vehicle)
    }

    override fun get(id: Int) = cars.find { it.id == id }
}

@Repository
class BicycleRepository : VehicleRepository<Bicycle> {
    val bicycles = mutableListOf(
        Bicycle(id = 1, brand = BicycleBrand.MONARK, year = 1987),
        Bicycle(id = 2, brand = BicycleBrand.CRESCENT, year = 1962)
    )

    override fun all() = bicycles.toList()

    override fun add(vehicle: Bicycle) {
        bicycles.add(vehicle)
    }

    override fun get(id: Int) = bicycles.find { it.id == id }
}

data class Bicycle @JsonCreator constructor(
    @JsonProperty("id") override val id: Int,
    @JsonProperty("brand") override val brand: BicycleBrand,
    @JsonProperty("year") override val year: Int,
    @JsonProperty("noWheels") val noWheels: Int = 2) : Vehicle<Bicycle> {
    override val type: Type
        get() = Type.BICYCLE
}

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseBody
    fun illegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
        this.logger.warn("IllegalArgumentException: ", e)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.localizedMessage)
    }

}

enum class CarBrand : VehicleBrand{
    VOLVO,
    PORSCHE,
    RELIANT_ROBIN
}

enum class BicycleBrand : VehicleBrand{
    MONARK,
    CRESCENT
}

interface VehicleBrand

data class Car @JsonCreator constructor(
    @JsonProperty("id") override val id: Int,
    @JsonProperty("brand") override val brand: CarBrand,
    @JsonProperty("year") override val year: Int,
    @JsonProperty("noWheels") val noWheels: Int = 4) : Vehicle<Car> {
    override val type: Type
        get() = Type.CAR
}


enum class Type {
    CAR,
    BICYCLE
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = Car::class, name = "CAR"),
    JsonSubTypes.Type(value = Bicycle::class, name = "BICYCLE")
)
interface Vehicle<T : Vehicle<T>> {
    val id: Int
    val type: Type
    val brand: VehicleBrand
    val year: Int
}