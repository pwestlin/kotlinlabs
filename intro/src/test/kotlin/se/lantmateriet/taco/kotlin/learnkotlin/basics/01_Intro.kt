@file:Suppress("PackageName", "unused")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

// main
fun main(args: Array<String>) {
    println("Hello, Wisconsin!")
}

// Från och med Kotlin 1.3 kan main deklareras utan parametrar
fun main() {
    println("Pirkko Määttä!")
}

fun thisIsATopLevelFunction(i: Int): Int {
    return i * 2
}

class Foo

// Ja, man kan ha flera klasser per Kotlin-fil
class Bar