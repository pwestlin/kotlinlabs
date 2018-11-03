@file:Suppress("PackageName", "RemoveRedundantBackticks", "unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE", "UnnecessaryVariable")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.coroutines.countryinfo

import khttp.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis


class CountryInfoTest {

    fun getCountryCodesOfCountriesInEurope(): List<String> {
        val r = get("https://restcountries.eu/rest/v2/region/europe")
        return r.jsonArray.map { (it as JSONObject).get("alpha2Code") as String }
    }

    fun getCountryName(code: String): String {
        val format = DateTimeFormatter.ofPattern("MM:ss:SSS")
        val startTime = System.currentTimeMillis()
        println("getCountryName start: ${LocalDateTime.from(Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime()).format(format)}")
        try {
            val r = get("https://restcountries.eu/rest/v2/alpha/$code")
            return r.jsonObject.get("name") as String
/*
            doSillyStuff()
            return "foo"
*/
        } finally {
            val stopTime = System.currentTimeMillis()
            println("getCountryName stop: ${LocalDateTime.from(Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime()).format(format)}")
            println("getCountryName exec: ${stopTime - startTime} ms")
        }
    }

    fun doSillyStuff() {
        var counter = 0
        for (i in 1..5_000_000_000) {
            counter += 1
        }
    }

    @Test
    fun `sadgasdgas`() {
        runBlocking {
            /*
                        repeat(50) {
                            launch {
                                getCountryName("sdag")
                            }
                        }
            */
            GlobalScope.launch {
                getCountryName("sdag")
            }
            println("1")
            GlobalScope.launch {
                getCountryName("sdag")
            }
            println("2")
            GlobalScope.launch {
                getCountryName("sdag")
            }
            println("3")
            GlobalScope.launch {
                getCountryName("sdag")
            }
            println("4")

        }
    }

    @Test
    fun `get names of all countries in Europe`() {
        val codes = getCountryCodesOfCountriesInEurope()

        var names: List<String> = mutableListOf()
        val time = measureTimeMillis {
            names = codes.map { getCountryName(it) }
        }

        names.forEach { println(it) }
    }

    @Test
    fun `get names of all countries in Europe with coroutines`() {
        val codes = getCountryCodesOfCountriesInEurope()

        val names = mutableListOf<String>()
        val jobs = mutableListOf<Job>()
        val time = measureTimeMillis {
            runBlocking {
                codes.forEach {
                    val job = GlobalScope.launch {
                        names.add(getCountryName(it))
                    }
                    jobs.add(job)

                }
            }
        }
        var active = true;
        while (active) {
            active = jobs.find { !it.isCompleted } != null
        }
        println("Exec time: $time ms")
        names.forEach { println(it) }
    }
}

