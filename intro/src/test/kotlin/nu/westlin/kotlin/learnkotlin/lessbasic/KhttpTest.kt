@file:Suppress("PackageName", "RemoveRedundantBackticks", "unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE", "UnnecessaryVariable")

package nu.westlin.kotlin.learnkotlin.lessbasic

import khttp.get
import org.json.JSONObject
import org.junit.Test
import org.junit.Ignore

class KhttpTest {

    @Test
    fun `get something`() {
        val r = get("http://httpbin.org/ip")
        println("r = ${r}")
        println(r.statusCode)
        // 200
        println(r.headers["Content-Type"])
        // "application/json; charset=utf-8"
        println(r.text)
        // """{"type": "User"..."""
        // org.json.JSONObject
        println("r.jsonObject = ${r.jsonObject}")
        println(r.jsonObject.getString("origin"))
    }

    @Test
    @Ignore
    fun `get all countries in Europe`() {
        val r = get("https://restcountries.eu/rest/v2/region/europe")
        println("r = ${r}")
        println(r.statusCode)
        // 200
        println(r.headers["Content-Type"])
        // "application/json; charset=utf-8"
        println(r.text)
        // """{"type": "User"..."""
        // org.json.JSONObject
        println("r.jsonArray = ${r.jsonArray}")
        val jsonArray = r.jsonArray
        val countryCodes = r.jsonArray.map { (it as JSONObject).get("alpha2Code") }
    }
}