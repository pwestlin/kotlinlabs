@file:Suppress("unused")

package nu.westlin.kotlin.vehicle

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import nu.westlin.kotlin.vehicle.Brand.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @GetMapping("/Type/{type}/id/{id}")
    fun getByTypeAndId(@PathVariable type: Type, @PathVariable id: Int, response: HttpServletResponse): Vehicle<*>? {
        val vehicle = when (type) {
            Type.CAR -> carRepository.get(id)
            Type.BICYCLE -> bicycleRepository.get(id)
        }
        if (vehicle == null) response.status = HttpStatus.NOT_FOUND.value()
        return vehicle
    }

    @GetMapping("car/{id}")
    // TODO petves: 404 if null?
    fun getCar(@PathVariable id: Int, response: HttpServletResponse): Car? {
        val car = carRepository.get(id)
        if (car == null) response.status = HttpStatus.NOT_FOUND.value()

        return car
    }

    @GetMapping("cars")
    fun getAllCars() = carRepository.all()

    @GetMapping("vehicles")
    fun getAllVehicles(): List<Vehicle<*>> {
        return carRepository.all().union(bicycleRepository.all()).toList()
    }

}

// TODO petves: Interface?
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
    val cars = mutableListOf(
        Bicycle(id = 1, brand = MONARK, year = 1987),
        Bicycle(id = 2, brand = CRESCENT, year = 1962)
    )

    override fun all() = cars.toList()

    override fun add(vehicle: Bicycle) {
        cars.add(vehicle)
    }

    override fun get(id: Int) = cars.find { it.id == id }
}

class Bicycle @JsonCreator constructor(
    @JsonProperty("id") id: Int,
    @JsonProperty("brand") brand: Brand,
    @JsonProperty("year") year: Int,
    @JsonProperty("noWheels") val noWheels: Int = 2) : Vehicle<Bicycle>(id, Type.BICYCLE, brand, year
)

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseBody
    fun illegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
        this.logger.warn("IllegalArgumentException: ", e)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.localizedMessage)
    }

}

// TODO petves: Cars an bicycles mixed is bad. :)
enum class Brand {
    VOLVO,
    PORSCHE,
    RELIANT_ROBIN,
    MONARK,
    CRESCENT
}

class Car @JsonCreator constructor(
    @JsonProperty("id") id: Int,
    @JsonProperty("brand") brand: Brand,
    @JsonProperty("year") year: Int,
    @JsonProperty("noWheels") val noWheels: Int = 4) : Vehicle<Car>(id, Type.CAR, brand, year
)

enum class Type {
    CAR,
    BICYCLE
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = Car::class, name = "CAR"),
    JsonSubTypes.Type(value = Bicycle::class, name = "BICYCLE")
)
abstract class Vehicle<T : Vehicle<T>>(val id: Int, val type: Type, val brand: Brand, val year: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vehicle<*>

        if (id != other.id) return false
        if (type != other.type) return false
        if (brand != other.brand) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + type.hashCode()
        result = 31 * result + brand.hashCode()
        result = 31 * result + year
        return result
    }
}