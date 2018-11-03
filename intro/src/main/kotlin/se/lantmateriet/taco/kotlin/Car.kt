package se.lantmateriet.taco.kotlin

import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    //cars()
    karts()
    //collections()
    //streams()
}

fun streams() {
    val list = listOf(1, 2, 3, 4, 5)
    val time = measureTimeMillis {
        list.asSequence()
            .map(expensiveOperation())
            .map(anotherExpensiveOperation())
            .first()
    }
    println("time = ${time}")
}

fun collections() {
    val strings = listOf("foo", "bar", "barabenpåglennhysen")

    val time = measureTimeMillis {
        val filtered = strings
            //.map(::expensiveMapOperation)
            //.map(::anotherExpensiveMapOperation)
            .toList()
        println("filtered = ${filtered}")
    }
    println("time = ${time}")

}

fun expensiveMapOperation(): (String) -> String {
    return {
        Thread.sleep(1000)
        it + it
    }
}

fun anotherExpensiveMapOperation(): (String) -> String {
    return {
        Thread.sleep(1000)
        it + it
    }
}

fun cars() {
    val car = Car("Opel", "Ascona", "1977")
    println("car = ${car}")
    val car2 = Car("Opel", "Ascona", "1977")
    println("car2.equals(car) = ${car2 == car}")
    println("car.copy() = ${car.copy()}")
    println("car == car.copy() = ${car == car.copy()}")
    println("car === car.copy() = ${car === car.copy()}")
    val car3 = car
    println("car3 == car = ${car3 == car}")
    println("car3 === car = ${car3 === car}")
}

fun karts() {
    val kart = Kart("Energy", "KZ2", "2014")
    println("kart = ${kart}")
    val kart2 = Kart("Energy", "KZ2", "2014")
    println("kart2 == kart = ${kart2 == kart}")
    println("kart2 === kart = ${kart2 === kart}")
}

private fun anotherExpensiveOperation(): (Int) -> Int {
    return {
        Thread.sleep(1000)
        it + 2
    }
}

private fun expensiveOperation(): (Int) -> Int {
    return {
        Thread.sleep(1000)
        it * 2
    }
}

data class Car(val brand: String, val model: String, val year: String)

class Kart(val brand: String, val model: String, val year: String)