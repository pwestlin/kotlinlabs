package nu.westlin.kotlin.testmocking

import nu.westlin.kotlin.testmocking.Car.Brand
import nu.westlin.kotlin.testmocking.Car.Brand.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestmockingApplication

fun main(args: Array<String>) {
    runApplication<TestmockingApplication>(*args)
}


data class Car(val brand: Brand, val productionYear: Int) {
    enum class Brand {
        VOLVO, PORSCHE, BMW
    }

}

class CarRepository {
    private val cars = mutableListOf<Car>(
        Car(VOLVO, 2017),
        Car(VOLVO, 2018),
        Car(BMW, 2018),
        Car(BMW, 2016),
        Car(PORSCHE, 2016),
        Car(PORSCHE, 2017),
        Car(PORSCHE, 2018)
    )

    fun add(car: Car) {
        cars.add(car)
    }

    fun remove(car: Car) {
        cars.remove(car)
    }

    fun all() = cars

    fun find(brand: Brand) = cars.filter { it.brand == brand }
    fun find(productionYear: Int) = cars.filter { it.productionYear == productionYear }
    fun find(predicate: (Car) -> Boolean) = cars.filter(predicate)

}

class CarDealerService(private val carRepository: CarRepository) {

    fun sell(car: Car) {
        carRepository.add(car)
    }

    fun buy(car: Car) {
        carRepository.remove(car)
    }

    fun list() = carRepository.all()
    //fun listBrands(brand: Brand) = carRepository.get()
    fun findByBrand(brand: Brand) = carRepository.find(brand)
    fun findByProductionYear(productionYear: Int) = carRepository.find(productionYear)
    fun findByBrandAndProductionYear(brand: Brand, productionYear: Int) =
        carRepository.find { car -> car.productionYear == productionYear && car.brand == brand }
}