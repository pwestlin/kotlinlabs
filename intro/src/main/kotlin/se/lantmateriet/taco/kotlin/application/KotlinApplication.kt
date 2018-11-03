package se.lantmateriet.taco.kotlin.application

import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@SpringBootApplication
class KotlinApplication

fun main(args: Array<String>) {
    val application = runApplication<KotlinApplication>(*args)
    val service = application.getBean<GreetingService>()
    println(service.greet("Sune"))
}

@RestController
@RequestMapping("/")
class GreetingController(val service: GreetingService) {
    @GetMapping(value = ["greet/{name}"], produces = ["application/json"])
    fun greet(@PathVariable name: String) = service.greet(name)
}

@Service
class GreetingService(val repo: GreetingRepository) {

    fun greet(name: String) =
        "${repo.greeting()}, $name!"
}

@Repository
class GreetingRepository {
    private val repo = listOf("Hi", "Yo", "Hello")

    fun greeting(): String {
        return repo[Random().nextInt(repo.size)]
    }
}

