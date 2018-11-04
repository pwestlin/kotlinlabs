@file:Suppress("unused")

package nu.westlin.kotlin.vehicle

import nu.westlin.kotlin.vehicle.Brand.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KClass


@SpringBootApplication
class DatabaseApplication

fun main(args: Array<String>) {
    runApplication<DatabaseApplication>(*args)
}


@RestController
@RequestMapping("/")
class VehicleController(private val repository: JdbcVehicleRepository) {

    @GetMapping("vehicle/{id}")
    // TODO petves: 404 if null?
    fun get(@PathVariable id: Int, response: HttpServletResponse): Vehicle? {
        val vehicle = repository.get(id)
        if (vehicle == null) response.status = HttpStatus.NOT_FOUND.value()

        return vehicle
    }

    @GetMapping("vehicles")
    fun getAll() = repository.all()

    @GetMapping("vehicles/type/{type}")
    fun getByType(@PathVariable type: String): List<Vehicle> {
        return repository.findBy(Type.valueOf(type))
    }

    @GetMapping("vehicles/classType/{classType}")
    fun getByClassType(@PathVariable classType: String): List<Vehicle> {
        return repository.findBy(getClassType(classType))
    }

    private fun getClassType(type: String): KClass<out Vehicle> {
        return when (type) {
            "Car" -> Car::class
            "Bicycle" -> Bicycle::class
            else -> throw IllegalArgumentException("\"$type\" is not a known type")
        }
    }
}

@Repository
class JdbcVehicleRepository {
    val vehicles = mutableListOf(
        Car(id = 1, brand = VOLVO, year = 1987),
        Car(id = 2, brand = PORSCHE, year = 1962),
        Car(id = 3, brand = RELIANT_ROBIN, year = 1993, noWheels = 3),
        Bicycle(id = 4, brand = MONARK, year = 1979)
    )

    fun all() = vehicles.toList()

    fun add(vehicle: Vehicle) {
        vehicles.add(vehicle)
    }

    fun get(id: Int) = vehicles.find { it.id == id }
    fun findBy(type: KClass<out Vehicle>): List<Vehicle> {
        return vehicles.filter { it::class == type }
    }

    fun findBy(type: Type): List<Vehicle> {
        return vehicles.filter { it.type == type }
    }
}

enum class Brand {
    VOLVO,
    PORSCHE,
    RELIANT_ROBIN,
    MONARK
}

enum class Type {
    CAR,
    BICYCLE
}

open class Vehicle(val id: Int, val type: Type, val brand: Brand, val year: Int)
class Car(id: Int, brand: Brand, year: Int, val noWheels: Int = 4) : Vehicle(id, Type.CAR, brand, year)
class Bicycle(id: Int, brand: Brand, year: Int, val noWheels: Int = 2) : Vehicle(id, Type.BICYCLE, brand, year)

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseBody
    fun illegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
        this.logger.warn("IllegalArgumentException: ", e)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.localizedMessage)
    }

}